kakao.maps.load(function() {
    let map, markers = {}, registeredShops = [];

    // 지도 초기화
    map = new kakao.maps.Map(document.getElementById('map'), {
        center: new kakao.maps.LatLng(37.5665, 126.9780),
        level: 4
    });

    // 등록 DB 정비소 마커
    $.ajax({
        url: '/rest-api/owner/car-center/list',
        method: 'GET',
        success: function(data) {
            registeredShops = data;
            registeredShops.forEach(shop => {
                const marker = new kakao.maps.Marker({
                    position: new kakao.maps.LatLng(shop.latitude, shop.longitude),
                    title: shop.name
                });
                marker.setMap(map);
                markers[shop.id] = marker;
                kakao.maps.event.addListener(marker, 'click', () => showShopDetail(shop, true));
            });

        },
        error: function(err) {
            console.error(err);
        }
    });

    // 정비소 상세정보 표시 + 예약폼 노출
    function showShopDetail(shop, isRegistered = true) {

        console.log(shop);
        const tmpFacilities = ["와이파이", "대기실"];
        const facilitiesHtml = (tmpFacilities || []).map(f => `<span class="pill">${f}</span>`).join('');
        const reserveText = isRegistered ? '<b style="color:green">예약 가능</b>' : '<b style="color:red">예약 불가</b>';

        document.getElementById('shop-detail').innerHTML = `
                <h3>${shop.name}</h3>
                <p>${reserveText}</p>
                <p><b>주소:</b> ${shop.address}</p>
                <p><b>☎:</b> ${shop.telNo || '정보 없음'}</p>
                ${shop.email ? `<p><b>Email:</b> ${shop.email}</p>` : ''}
                ${shop.bizNo ? `<p><b>사업자번호:</b> ${shop.bizNo}</p>` : ''}
                ${shop.desc ? `<p>${shop.desc}</p>` : ''}
                <div class="pillset">${facilitiesHtml}</div>
            `;
        const reservationCard = document.getElementById('reservation-card');
        if (isRegistered) {
            reservationCard.style.display = 'block';
            document.getElementById('selected-shop').value = shop.name;
            document.getElementById('car-center-id').value = shop.id || '';
        } else {
            reservationCard.style.display = 'none';
        }
    }

    // 검색 버튼 클릭 시
    document.getElementById('search-btn').addEventListener('click', function() {
        const keyword = document.getElementById('search-input').value.trim();
        if (!keyword) return alert('검색어를 입력하세요');

        // kakao.maps.services.Places 서비스 객체 생성
        const ps = new kakao.maps.services.Places(map);

        // 정비소 검색
        ps.keywordSearch(keyword + ' 정비소', function(data, status) {
            if (status !== kakao.maps.services.Status.OK) return;

            // 기존 검색 마커 제거
            Object.keys(markers).forEach(k => { if (k.startsWith('search-')) markers[k].setMap(null); });

            // 검색된 결과 마커 표시
            data.forEach((place, index) => {
                const isReg = registeredShops.some(s => s.name === place.place_name);
                if (isReg) return;

                const marker = new kakao.maps.Marker({
                    position: new kakao.maps.LatLng(place.y, place.x),
                    title: place.place_name
                });
                marker.setMap(map);
                markers['search-' + index] = marker;

                kakao.maps.event.addListener(marker, 'click', function() {
                    showShopDetail({
                        name: place.place_name,
                        address: place.road_address_name || place.address_name,
                        phone: place.phone
                    }, false);
                });
            });

            // 지도 중심을 검색된 장소로 이동
            if (data.length > 0) map.setCenter(new kakao.maps.LatLng(data[0].y, data[0].x));
        });
    });
});
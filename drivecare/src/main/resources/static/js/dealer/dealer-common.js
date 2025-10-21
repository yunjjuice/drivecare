(function() {

    // 기본 내비게이션
    const nav = document.getElementById('nav');
    const sections = [...document.querySelectorAll('.section')];
    nav.addEventListener('click', (e)=>{
        const btn = e.target.closest('button[data-section]');
        if(!btn) return;
        const id = btn.dataset.section;
        document.querySelectorAll('.nav button').forEach(b=>b.classList.toggle('active', b===btn));
        sections.forEach(s=>s.classList.toggle('active', s.id===id));
    });

    // 토스트
    const toast = document.getElementById('toast');
    const showToast = (msg='저장되었습니다.')=>{
        toast.textContent = msg;
        toast.style.display='block';
        setTimeout(()=> toast.style.display='none', 1600);
    };

    // 영업시간 테이블 생성
    const days = ['월','화','수','목','금','토','일'];
    const hoursBody = document.getElementById('hoursBody');
    days.forEach((d,i)=>{
        const tr = document.createElement('tr');
        tr.innerHTML = `
        <td>${d}</td>
        <td><input type="text" placeholder="09:00" value="${i<5?'09:00':''}"></td>
        <td><input type="text" placeholder="18:00" value="${i<5?'18:00':''}"></td>
        <td><input type="checkbox" ${i>=6?'checked':''}></td>
      `;
        hoursBody.appendChild(tr);
    });
    document.getElementById('copyWeek').addEventListener('click', ()=>{
        const rows = hoursBody.querySelectorAll('tr');
        const src = rows[0];
        const open = src.children[1].querySelector('input').value;
        const close = src.children[2].querySelector('input').value;
        rows.forEach((r,idx)=>{ if(idx<5){ r.children[1].querySelector('input').value=open; r.children[2].querySelector('input').value=close; r.children[3].querySelector('input').checked=false; } });
        showToast('평일 시간 복사 완료');
    });

    // 저장 핸들러 (로컬 스토리지)
    document.querySelectorAll('[data-save]').forEach(btn=>{
        btn.addEventListener('click', ()=>{
            const type = btn.dataset.save;
            if(type==='profile'){
                const data = {
                    shopName: document.getElementById('shopName').value,
                    bizNo: document.getElementById('bizNo').value,
                    phone: document.getElementById('phone').value,
                    email: document.getElementById('email').value,
                    address: document.getElementById('address').value,
                    about: document.getElementById('about').value
                };
                localStorage.setItem('profile', JSON.stringify(data));
            }else if(type==='hours'){
                const rows = [...hoursBody.querySelectorAll('tr')].map(r=>({
                    day: r.children[0].textContent,
                    open: r.children[1].querySelector('input').value,
                    close: r.children[2].querySelector('input').value,
                    off: r.children[3].querySelector('input').checked
                }));
                localStorage.setItem('hours', JSON.stringify(rows));
            }else if(type==='services'){
                localStorage.setItem('services', document.getElementById('svcTable').querySelector('tbody').innerHTML);
            }else if(type==='inventory'){
                localStorage.setItem('inventory', document.getElementById('invBody').innerHTML);
            }else if(type==='settings'){
                const settings = {
                    sms: document.querySelector('.switch[data-toggle="sms"]').classList.contains('on'),
                    autoConfirm: document.querySelector('.switch[data-toggle="autoConfirm"]').classList.contains('on')
                };
                localStorage.setItem('settings', JSON.stringify(settings));
            }
            showToast();
        });
    });

    // 스위치
    document.querySelectorAll('.switch').forEach(sw=>{
        sw.addEventListener('click', ()=> sw.classList.toggle('on'));
    });

    // 데모 데이터 채우기
    document.querySelector('[data-demo="fill"]').addEventListener('click', ()=>{
        document.getElementById('shopName').value='길동모터스';
        document.getElementById('bizNo').value='123-45-67890';
        document.getElementById('phone').value='02-123-4567';
        document.getElementById('email').value='hello@gd-motors.co.kr';
        document.getElementById('address').value='서울특별시 중구 을지로 100';
        document.getElementById('about').value='수입/국산 차량 정비 전문. 합리적 가격과 투명한 작업을 약속합니다.';
        showToast('데모 정보 입력 완료');
    });

    // 갤러리 업로드 미리보기
    const fileInput = document.getElementById('fileInput');
    const gallery = document.getElementById('gallery');
    fileInput.addEventListener('change', (e)=>{
        [...e.target.files].forEach(file=>{
            const url = URL.createObjectURL(file);
            const tile = document.createElement('div');
            tile.className='upload-tile';
            tile.style.backgroundImage=`url(${url})`;
            tile.style.backgroundSize='cover';
            tile.style.backgroundPosition='center';
            tile.innerHTML = '';
            gallery.appendChild(tile);
        });
    });

    // 서비스 표
    const svcTableBody = document.querySelector('#svcTable tbody');
    function addServiceRow(name, mins, price){
        const tr = document.createElement('tr');
        tr.innerHTML = `
        <td>${name}</td>
        <td>${mins}분</td>
        <td>₩${Number(price).toLocaleString()}</td>
        <td><button class="btn danger">삭제</button></td>
      `;
        tr.querySelector('button').addEventListener('click', ()=> tr.remove());
        svcTableBody.appendChild(tr);
    }
    document.getElementById('addSvc').addEventListener('click', ()=>{
        const n = document.getElementById('svcName').value.trim();
        const m = document.getElementById('svcMins').value || 30;
        const p = document.getElementById('svcPrice').value || 30000;
        if(!n) return showToast('서비스명을 입력하세요.');
        addServiceRow(n, m, p);
        document.getElementById('svcName').value='';
        document.getElementById('svcMins').value='';
        document.getElementById('svcPrice').value='';
    });

    // 예약 필터(프론트 데모)
    document.getElementById('bkFilter').addEventListener('click', ()=>{
        const q = (document.getElementById('bkSearch').value||'').toLowerCase();
        const st = document.getElementById('bkStatus').value;
        const rows = document.querySelectorAll('#bkTable tbody tr');
        rows.forEach(r=>{
            const text = r.textContent.toLowerCase();
            const statusEl = r.querySelector('.status');
            const has = !q || text.includes(q);
            const stOk = !st || statusEl.classList.contains(st);
            r.style.display = (has && stOk) ? '' : 'none';
        });
    });

    // 직원 추가
    document.getElementById('stAdd').addEventListener('click', ()=>{
        const n = document.getElementById('stName').value.trim();
        const role = document.getElementById('stRole').value.trim();
        const tel = document.getElementById('stPhone').value.trim();
        if(!n) return showToast('직원 이름을 입력하세요.');
        const tr = document.createElement('tr');
        tr.innerHTML = `<td>${n}</td><td>${role||'-'}</td><td>${tel||'-'}</td><td><span class="badge">오프</span></td><td><button class="btn danger">삭제</button></td>`;
        tr.querySelector('button').addEventListener('click', ()=> tr.remove());
        document.querySelector('#stTable tbody').appendChild(tr);
        document.getElementById('stName').value='';
        document.getElementById('stRole').value='';
        document.getElementById('stPhone').value='';
    });

    // 인벤토리 추가
    document.getElementById('invAdd').addEventListener('click', ()=>{
        const name = prompt('품목명'); if(!name) return;
        const brand = prompt('브랜드')||'-';
        const qty = prompt('재고 수량', '0')||'0';
        const min = prompt('최소 수량', '0')||'0';
        const price = prompt('단가(원)', '0')||'0';
        const tr = document.createElement('tr');
        tr.innerHTML = `<td>${name}</td><td>${brand}</td><td>${qty}</td><td>${min}</td><td>${Number(price).toLocaleString()}</td><td><button class="btn">발주</button></td>`;
        document.getElementById('invBody').appendChild(tr);
    });

    // 간단 차트(캔버스 2D, 라이브러리 없이)
    function simpleLine(canvasId, data){
        const c = document.getElementById(canvasId), ctx = c.getContext('2d');
        const w = c.width = c.clientWidth, h = c.height = 160;
        ctx.clearRect(0,0,w,h);
        // 축
        ctx.strokeStyle = '#263052'; ctx.lineWidth = 1;
        for(let i=0;i<=4;i++){
            const y = (h-20) - (i*(h-40)/4);
            ctx.beginPath(); ctx.moveTo(30,y); ctx.lineTo(w-10,y); ctx.stroke();
        }
        // 데이터
        const max = Math.max(...data); const min = Math.min(...data);
        const padX = 40; const plotW = w - padX - 10;
        ctx.beginPath(); ctx.lineWidth = 2; ctx.strokeStyle = '#7af0b3';
        data.forEach((v,idx)=>{
            const x = padX + (plotW*(idx/(data.length-1)));
            const y = (h-20) - ((v-min)/(max-min||1))*(h-40);
            if(idx===0) ctx.moveTo(x,y); else ctx.lineTo(x,y);
        });
        ctx.stroke();
        // 점
        ctx.fillStyle = '#5dd0ff';
        data.forEach((v,idx)=>{
            const x = padX + (plotW*(idx/(data.length-1)));
            const y = (h-20) - ((v-min)/(max-min||1))*(h-40);
            ctx.beginPath(); ctx.arc(x,y,3,0,Math.PI*2); ctx.fill();
        });
    }
    function simpleBar(canvasId, data){
        const c = document.getElementById(canvasId), ctx = c.getContext('2d');
        const w = c.width = c.clientWidth, h = c.height = 160;
        ctx.clearRect(0,0,w,h);
        const max = Math.max(...data);
        const pad = 30; const ww = (w - pad*2) / data.length;
        data.forEach((v,i)=>{
            const bh = ((v)/(max||1))*(h-40);
            ctx.fillStyle = '#5dd0ff';
            ctx.fillRect(pad + i*ww + 6, (h-20)-bh, ww-12, bh);
        });
        // 바닥선
        ctx.strokeStyle = '#263052'; ctx.beginPath(); ctx.moveTo(20,h-20); ctx.lineTo(w-10,h-20); ctx.stroke();
    }
    // 데모 데이터로 차트 그리기
    const bookingsData = [32,40,36,58,62,71,69,75,81,78,84,96];
    const revenueData  = [420,510,480,720,760,820,790,910,980,940,1030,1180];
    simpleLine('chartBookings', bookingsData);
    simpleBar('chartRevenue', revenueData);

    // 저장된 상태 복원
    window.addEventListener('load', ()=>{
        const p = localStorage.getItem('profile');
        if(p){
            const d = JSON.parse(p);
            ['shopName','bizNo','phone','email','address','about'].forEach(id=>{
                if(d[id]) document.getElementById(id).value = d[id];
            });
        }
        const h = localStorage.getItem('hours');
        if(h){
            const arr = JSON.parse(h);
            arr.forEach((row,i)=>{
                const tr = hoursBody.children[i];
                tr.children[1].querySelector('input').value = row.open;
                tr.children[2].querySelector('input').value = row.close;
                tr.children[3].querySelector('input').checked = row.off;
            });
        }
        const svcHtml = localStorage.getItem('services');
        if(svcHtml){ document.querySelector('#svcTable tbody').innerHTML = svcHtml; }
        const invHtml = localStorage.getItem('inventory');
        if(invHtml){ document.getElementById('invBody').innerHTML = invHtml; }
        const settings = localStorage.getItem('settings');
        if(settings){
            const s = JSON.parse(settings);
            if(s.sms) document.querySelector('.switch[data-toggle="sms"]').classList.add('on');
            if(s.autoConfirm) document.querySelector('.switch[data-toggle="autoConfirm"]').classList.add('on');
        }
    });

    // 접근성: 키보드 포커스 표시
    document.addEventListener('keydown', e=>{
        if(e.key==='Tab') document.body.classList.add('show-focus');
    });
})();
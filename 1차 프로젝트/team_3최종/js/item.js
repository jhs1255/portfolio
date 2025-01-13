var items = [
    {src:'./image/cloneball.gif', type:'plusball'},
    {src:'./image/barplus.gif', type:'plusbar'},
    {src:'./image/jump.gif', type:'jump'},
    {src:'./image/plusheart.gif', type:'plusheart'},
    {src:'./image/speed.gif', type:'speed'}
];

const itemPercent = {
    plusball: 0.05,    // 5% 확률
    plusbar: 0.05,     // 5% 확률
    jump: 0.01,        // 1% 확률
    plusheart: 0.02,   // 2% 확률
    speed: 0.1,       // 3% 확률
};
 

var itemfall = []; //떨어지는 아이템

//아이템 위치 업데이트
function upditemfall(){
    for (var i = 0; i < itemfall.length; i++) {
        var item = itemfall[i];
        item.y += 2; // 떨어지는 속도 조정

        // 바닥에 도달했는지 확인
        if (item.y + brickHeight > canvas.height) {
            item.status = 0; // 아이템 제거
            itemfall.splice(i, 1); // 리스트에서 제거
            i--; // 인덱스 조정
        }
    }
}

// 아이템과 패들의 충돌처리
function checkPaddleItem(){
    for (var i = 0; i < itemfall.length; i++) {
        var item = itemfall[i];
        if (item.y + brickHeight > canvas.height - paddleHeight && 
            item.x > paddleX && item.x < paddleX + paddleWidth) {
            // 아이템 충돌 처리
            itemimpact(item);
            itemfall.splice(i, 1); // 리스트에서 제거
            i--; // 인덱스 조정
        }
    }
}

//아이템 효과
function itemimpact(item){
    if (item.type === 'plusball') {
        // 공 추가
        
    } else if (item.type === 'plusbar') {
        // 바 길이 증가
        paddleX -= 10; // ★ 조금 더 자연스럽게 양 옆으로 길어지게 위치 조정.
        paddleWidth += 30;
    } else if (item.type === 'jump') {
        //다음 스테이지로 바로 넘어가기
        jumpItemEffect(); 
    } else if (item.type === 'plusheart') {
        // 남은 생명 1 증가.
        ballFall--;    // ★ 생명 증가.
        if (ballFall < 0){ // ★ 0보다 작으면 0으로 클리핑.
            ballFall = 0;
        }
        lifeDisplay();  // ★ 현재 생명 디스플레이 출력.
    } else if (item.type === 'speed') {
        // 속도 증가
        ball.speed += 2;    // ★ 속도 증가가 기하급수적으로 커져서 곱(*1.5)에서 합(+2)로 변경.

        // ★ 증가된 속도 적용.
        // ★ 현재 속도의 각도를 구하고, 단위 벡터를 구함.
        let deg = convertVectorToDegree(ball.speedX, ball.speedY).degree;
        let vec = convertDegreeToVector(deg);

        // ★ 계산한 단위 벡터에 증가된 속도를 곱함.
        ball.speedX = vec.x * ball.speed;
        ball.speedY = vec.y * ball.speed;
    }
}

// 아이템 그리기 함수 추가
function drawItemsFalling() {
    for (var i = 0; i < itemfall.length; i++) {
        var item = itemfall[i];
        var itemImage = new Image();
        itemImage.src = items.find(it => it.type === item.type).src;
        ctx.drawImage(itemImage, item.x, item.y, brickWidth, brickHeight);
    }
}

function jumpItemEffect() {
    // 모든 활성화된 벽돌 제거
    for (var c = 0; c < brickColumnCount; c++) {
        for (var r = 0; r < brickRowCount; r++) {
            // ★ [r][c]로 변경하고, 현재 상태가 1인 벽돌은 모두 제거하도록 변경.
            if (bricks[r][c].status === 1) {
                bricks[r][c].status = 0;
            }
        }
    }
    
    // 다음 스테이지로 이동
    alert('다음 스테이지로');
    nextstage();
}
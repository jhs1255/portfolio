/*-------------------------------------------------stage---------------------------------------- */
// 현재 스테이지 변수
let currentStage = 1;

// 게임판
const stage1_board = [
    [0,0,0,0,0, 0,0,0,1,0, 1,0,0,0,0, 0,0,0, 0,0,0,0,0],
    [0,0,0,0,0, 0,0,1,0,1, 0,1,0,0,0, 0,0,0, 0,0,0,0,0],
    [0,0,0,0,0, 0,0,1,0,1, 0,1,0,0,0, 0,0,0, 0,0,0,0,0],
    [0,0,0,0,0, 0,0,1,0,1, 0,1,0,0,0, 0,0,0, 0,0,0,0,0],
    [0,0,0,0,0, 0,1,0,0,0, 0,1,0,0,0, 0,0,0, 0,0,0,0,0],

    [0,0,0,0,0, 1,0,0,1,0, 0,0,1,1,1, 0,1,0, 0,0,0,0,0],
    [0,0,0,0,0, 1,0,0,1,0, 0,0,0,0,0, 1,0,1, 0,0,0,0,0],
    [0,0,0,0,0, 1,0,0,0,0, 0,0,0,0,0, 0,1,0, 0,0,0,0,0],
    [0,0,0,0,0, 1,1,0,0,0, 0,0,0,0,0, 0,1,0, 0,0,0,0,0],
    [0,0,0,0,0, 0,1,1,1,0, 0,0,1,1,0, 0,1,0, 0,0,0,0,0],

    [0,0,0,0,0, 0,0,1,0,0, 1,1,0,0,0, 1,0,0, 0,0,0,0,0],
    [0,0,0,0,0, 0,0,1,1,1, 0,1,1,1,1, 0,0,0, 0,0,0,0,0],
];
const stage2_board = [
    [0,0,0,0, 0,0,0,0,0, 0,0,1,1,1, 0,0,0,0, 0,0,0,0],
    [0,0,0,0, 0,0,0,0,0, 0,0,0,1,1, 1,0,0,0, 0,0,0,0],
    [0,0,0,0, 0,0,0,0,0, 0,0,1,1,1, 1,1,0,0, 0,0,0,0],
    [0,0,0,0, 0,0,0,0,0, 0,1,1,1,1, 0,1,1,1, 0,0,0,0],
    [0,0,0,0, 0,0,0,0,0, 0,1,1,1,1, 1,1,1,1, 0,0,0,0],

    [0,0,0,0, 0,0,0,0,0, 0,0,1,1,1, 1,1,0,0, 0,0,0,0],
    [0,0,0,0, 0,0,0,0,0, 0,0,0,1,1, 1,0,0,0, 0,0,0,0],
    [0,0,0,0, 0,0,0,0,0, 0,1,1,1,1, 1,1,0,0, 0,0,0,0],
    [0,0,0,0, 1,0,0,0,0, 1,1,1,1,1, 1,1,1,0, 0,0,0,0],
    [0,0,0,0, 1,1,0,1,1, 1,1,1,1,1, 1,1,1,0, 0,0,0,0],

    [0,0,0,0, 1,1,1,1,1, 0,1,1,1,1, 0,1,1,0, 0,0,0,0],
    [0,0,0,0, 0,1,1,1,1, 1,0,1,1,1, 0,1,1,0, 0,0,0,0],
    [0,0,0,0, 0,1,1,1,1, 1,1,0,0,0, 1,1,1,0, 0,0,0,0],
    [0,0,0,0, 0,0,1,1,1, 1,1,1,1,1, 1,1,0,0, 0,0,0,0],
    [0,0,0,0, 0,0,0,0,1, 1,1,1,1,1, 0,0,0,0, 0,0,0,0],
];
const stage3_board = [
    [0,0,0,0,0, 0,0,0,0,1, 1,1,0,0,0, 0,0,0,0,0, 0],
    [0,0,0,0,0, 0,0,0,1,1, 1,1,1,0,0, 0,0,0,0,0, 0],
    [0,0,0,0,1, 1,0,0,1,1, 1,1,1,0,0, 1,1,0,0,0, 0],
    [0,0,0,1,1, 1,1,0,1,1, 1,1,1,0,1, 1,1,1,0,0, 0],

    [0,0,0,1,1, 1,1,1,0,1, 1,1,0,1,1, 1,1,1,0,0, 0],
    [0,0,0,0,1, 1,1,1,0,1, 1,1,0,1,1, 1,1,0,0,0, 0],
    [0,0,0,0,0, 1,1,1,1,1, 1,1,1,1,1, 1,0,0,0,0, 0],
    [0,0,1,1,1, 0,0,1,1,0, 0,0,1,1,0, 0,1,1,1,0, 0],
    [0,1,1,1,1, 1,1,1,0,0, 1,0,0,1,1, 1,1,1,1,1, 0],
    
    [0,1,1,1,1, 1,1,1,0,1, 1,1,0,1,1, 1,1,1,1,1, 0],
    [0,1,1,1,1, 1,1,1,0,0, 1,0,0,1,1, 1,1,1,1,1, 0],
    [0,0,1,1,1, 0,0,1,1,0, 0,0,1,1,0, 0,1,1,1,0, 0],
    [0,0,0,0,0, 1,1,1,1,1, 1,1,1,1,1, 1,0,0,0,0, 0],
    [0,0,0,0,1, 1,1,1,0,1, 1,1,0,1,1, 1,1,0,0,0, 0],

    [0,0,0,1,1, 1,1,1,0,1, 1,1,0,1,1, 1,1,1,0,0, 0],
    [0,0,0,1,1, 1,1,0,1,1, 1,1,1,0,1, 1,1,1,0,0, 0],
    [0,0,0,0,1, 1,0,0,1,1, 1,1,1,0,0, 1,1,0,0,0, 0],
    [0,0,0,0,0, 0,0,0,1,1, 1,1,1,0,0, 0,0,0,0,0, 0],
    [0,0,0,0,0, 0,0,0,0,1, 1,1,0,0,0, 0,0,0,0,0, 0],
];

// 스테이지 정보 객체
const stage1 = {
    board: stage1_board,
    brickRowCount: stage1_board.length,
    brickColumnCount: stage1_board[0].length,
    clearScore:5000,
}
const stage2 = {
    board: stage2_board,
    brickRowCount: stage2_board.length,
    brickColumnCount: stage2_board[0].length,
    clearScore:10000,
}
const stage3 = {
    board: stage3_board,
    brickRowCount: stage3_board.length,
    brickColumnCount: stage3_board[0].length,
    clearScore:20000,
}

// 스테이지들 객체
const stages = {
    1: stage1,
    2: stage2,
    3: stage3,
};


/*--------------------------------------------벽돌 생성-------------------------------------------*/
// ★ 벽돌 관련 변수. 값 초기화를 initBricks()에서 하도록 변경.
var brickRowCount;      // 벽돌 배치 시 행의 개수.
var brickColumnCount;   // 벽돌 배치 시 열의 개수.
var brickPadding;       // 벽돌 간 간격.
var brickWidth;         // 벽돌 1개의 가로 길이.
var brickHeight;        // 벽돌 1개의 세로 길이.
var brickOffsetTop;     // 벽돌과 화면 상단 사이의 간격.
var brickOffsetLeft;    // 벽돌과 화면 좌측 사이의 간격.


var bricks = [];
initBricks();

// ★ 벽돌(bricks) 설정 초기화하는 함수.
function initBricks() {
    brickRowCount = stages[currentStage].brickRowCount;
    brickColumnCount = stages[currentStage].brickColumnCount;
    brickPadding = 0;
    brickWidth = (canvas.width - (brickColumnCount - 1) * brickPadding) / (brickColumnCount+1); // 캔버스 너비에 맞게. (1블럭 정도 띄움).
    brickHeight= (canvas.height - (brickRowCount - 1) * brickPadding) / (brickRowCount + 10);   // 캔버스 높이에 맞게. (바닥과 10블럭 정도 띄움).
    brickOffsetTop = Math.floor(brickHeight/2); // 벽돌과 화면 상단 사이의 간격 (블럭 높이 / 2).
    brickOffsetLeft = Math.floor((canvas.width - (((brickPadding + brickWidth) * (brickColumnCount - 1)) + brickWidth)) / 2);   // 벽돌과 화면 좌측 사이의 간격 (블럭 너비 / 2).

    // ★ bricks 생성 및 초기화, 사용 시 [r][c]로 해야함.
    for (let r = 0; r < brickRowCount; r++) {
        bricks[r] = [];
        for (let c = 0; c < brickColumnCount; c++) {
            bricks[r][c] = { x: 0, y: 0, status: 0, brickScore: 0, type: 'type' };
            
            // stage에 맞게 초기화.
            if (stages[currentStage].board[r][c] === 1){
                var brickX = (c * (brickWidth + brickPadding)) + brickOffsetLeft;
                var brickY = (r * (brickHeight + brickPadding)) + brickOffsetTop;
                
                bricks[r][c].x = brickX;
                bricks[r][c].y = brickY;
                bricks[r][c].status = 1;
                bricks[r][c].brickScore = 100;
                bricks[r][c].type= 'brick';

                // 아이템 블럭 생성
                if (Math.random() < itemPercent.plusball) {
                    // ★ 공 개수 증가는 아직 적용이 안 된 부분이라서 주석 처리... .
                    // bricks[r][c].type = 'plusball';
                } else if (Math.random() < itemPercent.plusbar) {
                    bricks[r][c].type = 'plusbar';
                } else if (Math.random() < itemPercent.jump) {
                    bricks[r][c].type = 'jump';
                } else if (Math.random() < itemPercent.plusheart) {
                    bricks[r][c].type = 'plusheart';
                } else if (Math.random() < itemPercent.speed) {
                    bricks[r][c].type = 'speed';
                }
            }
        }
    }
}

function collider() {
    for (var c = 0; c < brickColumnCount; c++) {
        for (var r = 0; r < brickRowCount; r++) {
            // ★ b 삭제. b에 대입하여 생성하면 벽돌이 중복으로 생성됨.
            if (bricks[r][c].status == 1) {
                // ★ [r][c]로 변경. collideBrick()으로 공과 벽돌이 충돌되었는지 감지.
                if (collideBrick(bricks[r][c]) === true) {
                    // 충돌한 벽돌의 타입이 아이템인 경우 처리.
                    if(bricks[r][c].type !== 'brick') {
                        itemfall.push({x:bricks[r][c].x, y:bricks[r][c].y, type:bricks[r][c].type});
                    }
                    
                    // 충돌 처리.
                    bricks[r][c].status = 0; // 벽돌 제거
                    addScore(bricks[r][c].brickScore);  // 점수 추가.
                    printScore();   // 점수 출력.
                    clearBrick();// 벽돌이 모두 제거되었는지 확인
                }
            }
        }
    }
}

// ★ 공이 벽돌에 충돌했는지 감지하는 함수. 충돌 시 true 리턴.
function collideBrick(brick) {
    let brickHalfWidth = brickWidth/2;
    let brickHalfHeight = brickHeight/2;

    let distX = Math.abs(ball.posX - (brick.x + brickHalfWidth));
    let distY = Math.abs(ball.posY - (brick.y + brickHalfHeight));

    // 충돌 X 경우,
    if (distX > (brickHalfWidth + ball.radius) || distY > (brickHalfHeight + ball.radius)) {
        return false;
    }
    
    // 벽면에 충돌한 경우,
    let isReflected = false;
    if (distX <= brickHalfWidth || distY <= brickHalfHeight) {
        let direction = getCollisionDirection(brick);
        
        if (direction == "left") {
            if (ball.speedX > 0) {
                ball.speedX = -ball.speedX;
                console.log("left");
                isReflected = true;
            }
        }
        else if (direction == "right") {
            if (ball.speedX < 0) {
                ball.speedX = -ball.speedX;
                console.log("right");
                isReflected = true;
            }
        }
        else if (direction == "top") {
            if (ball.speedY > 0) {
                ball.speedY = -ball.speedY;
                console.log("top");
                isReflected = true;
            }
        }
        else if (direction == "bottom") {
            if (ball.speedY < 0) {
                ball.speedY = -ball.speedY;
                console.log("bottom");
                isReflected = true;
            }
        }
    }
    if (isReflected === true) {
        return true;
    }

    let dx = distX - brickHalfWidth;
    let dy = distY - brickHalfHeight;
    // 모서리에 충돌한 경우,
    if (dx*dx+dy*dy<=(ball.radius*ball.radius)) {
        // 원이 벽돌에 닿은 경우,
        reflectBall(brick);
        console.log("corner true");
        return true;
    } else {
        // 원이 벽돌에 닿지 않는 경우,
        console.log("corner false");
        return false;
    }
}

// ★ 벽돌의 충돌 방향 구하는 함수.
function getCollisionDirection(brick) {

    let brickHalfWidth = brickWidth/2;
    let brickHalfHeight = brickHeight/2;

    let dx = (ball.posX - (brick.x + brickHalfWidth)) / (brickHalfWidth);
    let dy = (ball.posY - (brick.y + brickHalfHeight)) / (brickHalfHeight);

    if (Math.abs(dx) > Math.abs(dy)) {
        if (dx < 0) {
            return "left";
        } else {
            return "right";
        }
    } else {
        if (dy < 0) {
            return "top";
        } else {
            return "bottom";
        }
    }
}

// ★ 충돌 지점이 모서리일 때, 반사시키는 함수.
function reflectBall(brick) {
    // 충돌 당시의 직사각형의 중심과 원의 중심 사이의 거리.
    let distX = ball.posX - (brick.x + brickWidth/2);
    let distY = ball.posY - (brick.y + brickHeight/2);
    var distance = Math.sqrt(distX * distX + distY * distY);

    // 충돌 지점에서 직사각형의 중심까지의 단위 벡터.
    var unitX = distX / distance;
    var unitY = distY / distance;

    // 현재 공의 속도.
    var velocityX = ball.speedX;
    var velocityY = ball.speedY;

    // 충돌 시 반사 벡터 계산.
    var dotProduct = unitX * velocityX + unitY * velocityY;
    var reflectX = velocityX - 2 * dotProduct * unitX;
    var reflectY = velocityY - 2 * dotProduct * unitY;

    // 반사된 속도를 원의 속도에 반영
    ball.speedX = reflectX;
    ball.speedY = reflectY;
}

// ★ 현재 공의 속도(방향)을 통해 공의 각도를 구하는 함수.
function convertVectorToDegree(x, y) {
    const radian = Math.atan2(y,x);
    const degree = radian * (180 / Math.PI);

    //// console.log(`convertVectorToDegree - radian: ${radian}`);
    //// console.log(`convertVectorToDegree - degree: ${degree}`);

    return {"radian": radian, "degree": degree};
}
// ★ 현재 공의 각도를 통해 공의 방향(단위 벡터)을 구하는 함수.
function convertDegreeToVector(degree) {
    const radian = degree * (Math.PI / 180);

    const x = Math.cos(radian);
    const y = Math.sin(radian);

    //// console.log(`convertDegreeToVector -radian: ${radian}`);
    //// console.log(`convertDegreeToVector -vector x: ${x} y: ${y}`);
    
    return {"x": x, "y": y};
}

function clearBrick(){ //r,c 변경함
    for (var r = 0; r < brickRowCount; r++) {
        for (var c = 0; c < brickColumnCount; c++) {
            if (bricks[r][c].status == 1) { 
                return false;
            }
        }
    }
    addScore(stages[currentStage].clearScore);
    printScore();
    alert('다음 스테이지로');
    nextstage();
}

function nextstage() {
    if (currentStage < 3) {
        currentStage++;
        ballCount = 0;
        resetBall();

        // ★ 다음 스테이지로 벽돌 설정 초기화.
        initBricks();
    } else {
        clearInterval(intervalid);
        alert('end');
        document.location.reload();
    }
}

//-------------------------벽돌 그리기-----------------------
const ctxBrick = canvas.getContext("2d");   // ★ 벽돌 전용 ctx. 추가 및 drawBricks()에 적용.
function drawBricks() {
    for (var c = 0; c < brickColumnCount; c++) {
        for (var r = 0; r < brickRowCount; r++) {
            // ★ [r][c]로 변경하고, 맵의 배치와 관계없이, 현재 벽돌의 상태만 비교.
            if (bricks[r][c].status === 1) { // 벽돌이 현재 상태가 1인 경우
                var brickX = bricks[r][c].x;
                var brickY = bricks[r][c].y;

                // ★ 그리는 부분만 남기고, 설정하는 부분은 initBricks()로 나눔.
                ctxBrick.beginPath();
                ctxBrick.rect(brickX, brickY, brickWidth, brickHeight);
                ctxBrick.strokeRect(brickX, brickY, brickWidth, brickHeight);
                ctxBrick.closePath();
                ctxBrick.fillStyle = "white";
                ctxBrick.fill();
                ctxBrick.strokeStyle = 'black';
                ctxBrick.stroke();

                if (bricks[r][c].type !== 'brick') {
                    var itemImage = new Image();
                    itemImage.src = items.find(item => item.type === bricks[r][c].type).src;
                    ctxBrick.drawImage(itemImage, brickX, brickY, brickWidth, brickHeight);
                }
            }
        }
    }
}

drawBricks();
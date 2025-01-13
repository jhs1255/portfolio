document.addEventListener('DOMContentLoaded', (event) => {
    const buttons = document.querySelectorAll('.btn-group-vertical .btn');
    let currentIndex = 0;

    // Initial focus
    buttons[currentIndex].classList.add('active');
    buttons[currentIndex].focus();

    document.addEventListener('keydown', (e) => {
        if (e.key === 'ArrowDown') {
            buttons[currentIndex].classList.remove('active');
            currentIndex = (currentIndex + 1) % buttons.length;
            buttons[currentIndex].classList.add('active');
            buttons[currentIndex].focus();
        } else if (e.key === 'ArrowUp') {
            buttons[currentIndex].classList.remove('active');
            currentIndex = (currentIndex - 1 + buttons.length) % buttons.length;
            buttons[currentIndex].classList.add('active');
            buttons[currentIndex].focus();
        } else if (e.key === ' ') {
            buttons[currentIndex].click();
        }
    });

    // Event listeners for buttons
    document.getElementById('startButton').addEventListener('click', () => {
        location.href='swipe.html';
    });
    document.getElementById('descriptionButton').addEventListener('click', () => {

    });
    document.getElementById('settingsButton').addEventListener('click', () => {
        
    });

    // Save settings to localStorage
    document.getElementById('saveSettingsButton').addEventListener('click', () => {
        const ballColor = document.querySelector('input[name="ballColor"]:checked').value;
        const paddleColor = document.querySelector('input[name="paddleColor"]:checked').value;

        localStorage.setItem('ballColor', ballColor);
        localStorage.setItem('paddleColor', paddleColor);

        $('#settingsModal').modal('hide');
    });

});

function gameOver() {
    isGameRunning = false;
    $('#gameOverModal').modal('show');
}

// 이니셜과 점수를 입력받아 저장하는 함수
function saveInitials() {
    const initials = document.getElementById('initials').value;
    const score = getCurrentScore(); // 현재 점수를 가져오는 함수. 구현 필요.
    
    if (initials && score) {
        saveScore(initials, score);
        console.log('이니셜과 점수를 저장했습니다:', { initials, score });
    } else {
        console.error('이니셜 또는 점수를 확인해주세요.');
    }
    document.location.reload(); // 페이지 새로고침
}

// 점수를 저장하는 함수
function saveScore(initials, score) {
    // 로컬 스토리지에서 기존 점수 리스트를 가져옴
    let scores = JSON.parse(localStorage.getItem('scores')) || [];
    
    // 새로운 점수를 추가
    scores.push({ initials: initials, score: score });
    
    // 점수를 내림차순으로 정렬
    scores.sort((a, b) => b.score - a.score);
    
    // 로컬 스토리지에 점수 리스트 저장
    localStorage.setItem('scores', JSON.stringify(scores));

    displayHighscore(); // hightscore로 점수 넘겨주기
}

document.getElementById("saveInitialsButton").addEventListener("click", saveInitials);
document.getElementById("restartButton").addEventListener("click", ()=>{
    document.location.reload(); // 페이지 새로고침
});
document.getElementById("homeButtonGameOver").addEventListener("click", () => {
    location.href = 'index.html';
});

// 설정 모달창 저장 버튼
document.querySelector("#saveSettingsButton").addEventListener("click", function () {
    const selectedBallColor = document.querySelector('input[name="ballColor"]:checked').value;
    const selectedPaddleColor = document.querySelector('input[name="paddleColor"]:checked').value;
  
    ballColor = selectedBallColor;
    paddleColor = selectedPaddleColor;
  
    isSetting = false;
  
    $('#settingsModal').modal('hide');
  });
  
  document.querySelector("#closeSetting").addEventListener("click",function(){
    isSetting = false;
    $('#settingsModal').modal('hide');
  })

// 현재 점수를 가져오는 함수. 실제 게임 로직에 맞게 구현해야 함.
function getCurrentScore() {
    return score; 
}
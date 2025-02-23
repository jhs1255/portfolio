package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import dto.MovieData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class EasySelectMember implements Initializable{
    @FXML private StackPane selectMemberPane;
    @FXML private Button home; // 홈버튼
    @FXML private Button nextPage; // 다음으로 버튼 
    @FXML private Button backBtn; // 이전으로 버튼
    @FXML private VBox movieList; // 영화 목록 패널
    @FXML private Label selectTitle; // 영화제목 불러오기
	@FXML private Label selectTime;
	@FXML private ImageView selectPoster;

    //일반 인원 선택0~10명까지
	@FXML private Button adult,adult1,adult2,adult3,adult4,adult5,adult6,adult7,adult8,adult9,adult10;
	//청소년 인원 선택 0~10명까지
	@FXML private Button kids,kids1,kids2,kids3,kids4,kids5,kids6,kids7,kids8,kids9,kids10;
	//경로 인원 선택 0~10명까지
	@FXML private Button grand,grand1,grand2,grand3,grand4,grand5,grand6,grand7,grand8,grand9,grand10;
	//우대 인원 선택 0~10까지
	@FXML private Button special,special1,special2,special3,special4,special5,special6,special7,special8,special9,special10;
	
	//버튼을 배열로 관리
	private Button[] adultButtons;
	private Button[] kidsButtons;
	private Button[] grandButtons;
	private Button[] specialButtons;

    private MovieData selectedMovie; // 선택된 영화 정보를 저장하는 변수
    
    private Integer selectedNumberOfPeople; // 선택한 인원 수
    private Integer selectedTotalPrice; // 총 금액
    
    private Set<Button> selectedButtons = new HashSet<>();
    private List<String> selectedPeopleDetails = new ArrayList<>(); // 선택한 인원 정보를 저장할 리스트

     // 가격 배열: [연령대][영화 타입]
     private final int[][] prices = {
        {10000, 15000, 20000, 25000, 30000}, // Adult: 2D, 3D, 4D, 4Dx, IMAX 일반 금액
        {8000, 12000, 18000, 23000, 28000},  // Kids 청소년 금액
        {9000, 14000, 19000, 24000, 29000},  // Grand 경로 금액
        {9500, 13000, 20000, 25000, 30000}   // Special 우대 금액
    };


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        adultButtons = new Button[] {adult,adult1,adult2,adult3,adult4,adult5,adult6,adult7,adult8,adult9,adult10};
		kidsButtons = new Button[] {kids,kids1,kids2,kids3,kids4,kids5,kids6,kids7,kids8,kids9,kids10};
		grandButtons = new Button[] {grand,grand1,grand2,grand3,grand4,grand5,grand6,grand7,grand8,grand9,grand10};
		specialButtons = new Button[] {special,special1,special2,special3,special4,special5,special6,special7,special8,special9,special10};
		
		
		// 모든 버튼에 클릭 이벤트 핸들러 추가
        for (Button button : adultButtons) {
            button.setOnMouseClicked(event -> handleButtonClick(button));
        }
        // 모든 버튼에 클릭 이벤트 핸들러 추가
        for (Button button : kidsButtons) {
            button.setOnMouseClicked(event -> handleButtonClick(button));
        }
        
        // 모든 버튼에 클릭 이벤트 핸들러 추가
        for (Button button : grandButtons) {
            button.setOnMouseClicked(event -> handleButtonClick(button));
        }
        
        // 모든 버튼에 클릭 이벤트 핸들러 추가
        for (Button button : specialButtons) {
            button.setOnMouseClicked(event -> handleButtonClick(button));
        }
        home.setOnAction(event->BackHome(event));// home 버튼
		backBtn.setOnAction(event->BackPage(event)); //이전으로 버튼
        nextPage.setOnAction(event->NextPage(event)); //다음으로 버튼
        
    }
    //버튼 클릭 시 선택한 버튼 색상 변경 및 제거
		private void handleButtonClick(Button clickedButton) {
			
			String category = "";

		    if (Arrays.asList(adultButtons).contains(clickedButton)) {
		        category = "일반"; // Adult
		    } else if (Arrays.asList(kidsButtons).contains(clickedButton)) {
		        category = "청소년"; // Kids
		    } else if (Arrays.asList(grandButtons).contains(clickedButton)) {
		        category = "경로"; // Grand
		    } else if (Arrays.asList(specialButtons).contains(clickedButton)) {
		        category = "우대"; // Special
		    }
		    
			if (selectedButtons.contains(clickedButton)) {
	            // 이미 선택된 버튼이면 선택 해제
	            selectedButtons.remove(clickedButton);
	            clickedButton.getStyleClass().remove("selected");
	         // 선택 해제 시 리스트에서 제거
	            selectedPeopleDetails.remove(category+" "+clickedButton.getText());
	        } else {
	            // 선택되지 않은 버튼이면 선택 추가
	            selectedButtons.add(clickedButton);
	            clickedButton.getStyleClass().add("selected");
	         // 선택한 인원 정보를 리스트에 추가
	            selectedPeopleDetails.add(category+" "+clickedButton.getText());
	        }
	        updateSelectedPeopleAndPrice(); // 인원 수와 총 금액 계산
	        displaySelectedDetails(); // 선택한 인원 정보를 콘솔에 출력
		}
        private void updateSelectedPeopleAndPrice() {
		    selectedNumberOfPeople = 0;
		    selectedTotalPrice = 0;

		    for (Button button : selectedButtons) {
		        String buttonText = button.getText(); // 클릭한 버튼의 텍스트를 가져옴
		        int numberOfPeople = Integer.parseInt(buttonText); // 버튼 텍스트를 숫자로 변환
		        int ticketPrice = 0;

		        // 영화 타입에 따른 가격 인덱스 결정
		        int movieTypeIndex = getMovieTypeIndex(selectedMovie.getSelectedMovieType()); // 영화 타입에 따른 인덱스 얻기

		        // 연령대에 따라 가격 결정
		        if (Arrays.asList(adultButtons).contains(button)) {
		            ticketPrice = prices[0][movieTypeIndex]; // Adult 가격
		        } else if (Arrays.asList(kidsButtons).contains(button)) {
		            ticketPrice = prices[1][movieTypeIndex]; // Kids 가격
		        } else if (Arrays.asList(grandButtons).contains(button)) {
		            ticketPrice = prices[2][movieTypeIndex]; // Grand 가격
		        } else if (Arrays.asList(specialButtons).contains(button)) {
		            ticketPrice = prices[3][movieTypeIndex]; // Special 가격
		        }

		        selectedNumberOfPeople += numberOfPeople; // 총 인원 수 업데이트
		        selectedTotalPrice += ticketPrice * numberOfPeople; // 총 금액 계산
		    }
		}
        public void initializeData(MovieData selectMovie) {
            this.selectedMovie = selectMovie;
            
            if(selectTitle != null && selectTime != null && selectPoster != null) {
                selectTitle.setText("[" + selectedMovie.getSelectedMovieRating() +"] "+ selectedMovie.getSelectedMovieTitle()+" ("+ selectedMovie.getSelectedMovieType()+") "+selectedMovie.getSelectedMovieRuntime()+"분 "+"["+selectedMovie.getSelectedMovieSection()+"]");
                selectTime.setText(selectedMovie.getSelectedMovieStartTime());
                
                File file = new File(selectedMovie.getSelectedMoviePoster());
                Image image = new Image(file.toURI().toString());
                selectPoster.setImage(image);
                selectPoster.setFitHeight(170);
                selectPoster.setFitWidth(150);
            }
        }

        @FXML
        public void handleMemberSelect(ActionEvent event) {
            
        }
		private void displaySelectedDetails() {
	        System.out.println("선택한 인원:");
	        for (String detail : selectedPeopleDetails) {
	            System.out.println(detail); // 콘솔에 각 선택된 인원 출력
	        }
	        System.out.println("총 인원: " + selectedNumberOfPeople);
	        System.out.println("총 금액: " + selectedTotalPrice + "원");
	    }
		
		private int getMovieTypeIndex(String movieType) {
	        switch (movieType) {
	            case "2D": return 0;
	            case "3D": return 1;
	            case "4D": return 2;
	            case "4Dx": return 3;
	            case "IMAX": return 4;
	            default: return -1; // 예외 처리
	        }
	    }

        private void NextPage(ActionEvent event) {
            try {
                // FXML 파일을 로드하여 메뉴 화면으로 전환
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EasyChoiceSelect.fxml"));
                Parent homeRoot = loader.load();
                EasyChoiceSelect controller = loader.getController();
                controller.keeping(selectedMovie ,selectedMovie.getSelectedMovieSeat(),selectedNumberOfPeople, selectedTotalPrice, selectedPeopleDetails);
                // 현재 창의 Stage를 가져옴
                Stage stage = (Stage) home.getScene().getWindow();
                Scene scene = new Scene(homeRoot); // 홈화면으로 전환
    
                // 메뉴 화면으로 전환
                stage.setScene(scene);
                stage.show(); //화면에 출력
                
    
            } catch (Exception e) {
                e.printStackTrace();
                // 예외 발생 시 사용자에게 경고창 표시
            }
        }

        private void BackPage(ActionEvent event) {
            try {
                // FXML 파일을 로드하여 메뉴 화면으로 전환
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EasyTime.fxml"));
                Parent homeRoot = loader.load();
                // 현재 창의 Stage를 가져옴
                Stage stage = (Stage) home.getScene().getWindow();
                Scene scene = new Scene(homeRoot); // 홈화면으로 전환
    
                // 메뉴 화면으로 전환
                stage.setScene(scene);
                stage.show(); //화면에 출력
                
    
            } catch (Exception e) {
                e.printStackTrace();
                // 예외 발생 시 사용자에게 경고창 표시
            }
        }
        private void BackHome(ActionEvent event) {
            try {
                // FXML 파일을 로드하여 메뉴 화면으로 전환
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EasyMonitor.fxml"));
                Parent homeRoot = loader.load();
                // 현재 창의 Stage를 가져옴
                Stage stage = (Stage) home.getScene().getWindow();
                Scene scene = new Scene(homeRoot); // 홈화면으로 전환
    
                // 메뉴 화면으로 전환
                stage.setScene(scene);
                stage.show(); //화면에 출력
                
    
            } catch (Exception e) {
                e.printStackTrace();
                // 예외 발생 시 사용자에게 경고창 표시
            }
        }
}

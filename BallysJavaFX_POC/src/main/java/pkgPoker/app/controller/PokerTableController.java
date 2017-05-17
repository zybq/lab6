package pkgPoker.app.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurve;
import javafx.util.Duration;
import pkgPoker.app.MainApp;

public class PokerTableController implements Initializable {

	// Reference to the main application.
	private MainApp mainApp;

	public PokerTableController() {
	}

	@FXML
	private AnchorPane mainAnchor;

	@FXML
	private Label lblWinningPlayer;
	@FXML
	private Label lblWinningHand;

	@FXML
	private Label lblPlayerPos1;
	@FXML
	private Label lblPlayerPos2;

	@FXML
	private ImageView imgViewDealerButtonPos1;
	@FXML
	private ImageView imgViewDealerButtonPos2;

	@FXML
	private BorderPane OuterBorderPane;

	@FXML
	private Label lblNumberOfPlayers;
	@FXML
	private TextArea txtPlayerArea;

	@FXML
	private Button btnStartGame;

	private ArrayList<ToggleButton> btnSitLeave = new ArrayList<ToggleButton>();
	@FXML
	private ToggleButton btnPos1SitLeave;
	@FXML
	private ToggleButton btnPos2SitLeave;

	@FXML
	private Label lblPos1Name;
	@FXML
	private Label lblPos2Name;

	@FXML
	private HBox hBoxDeck;

	@FXML
	private HBox hboxP1Cards;
	@FXML
	private HBox hboxP2Cards;

	@FXML
	private HBox hboxDeck;

	@FXML
	private HBox hboxCommunity;

	private int iDrawCard = 0;

	private int iAnimationLength = 250;

	public void DealFakeHand(ActionEvent event) {

		hboxP1Cards.getChildren().clear();

		ImageView i1 = new ImageView(
				new Image(getClass().getResourceAsStream("/include/img/26.png"), 75, 75, true, true));
		hboxP1Cards.getChildren().add(i1);

		ImageView i2 = new ImageView(
				new Image(getClass().getResourceAsStream("/include/img/27.png"), 75, 75, true, true));
		hboxP1Cards.getChildren().add(i2);

		ImageView i3 = new ImageView(
				new Image(getClass().getResourceAsStream("/include/img/28.png"), 75, 75, true, true));
		hboxP1Cards.getChildren().add(i3);

		ImageView i4 = new ImageView(
				new Image(getClass().getResourceAsStream("/include/img/29.png"), 75, 75, true, true));
		hboxP1Cards.getChildren().add(i4);

		ImageView i5 = new ImageView(
				new Image(getClass().getResourceAsStream("/include/img/30.png"), 75, 75, true, true));
		hboxP1Cards.getChildren().add(i5);

	}

	public void initialize(URL location, ResourceBundle resources) {
		btnSitLeave.clear();
		btnSitLeave.add(btnPos1SitLeave);
		btnSitLeave.add(btnPos2SitLeave);
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	@FXML
	private void handlePlay() {
	}

	@FXML
	public void GetGameState() {
	}

	public void btnSitLeave_Click(ActionEvent event) {
	}

	public void MessageFromMainApp(String strMessage) {
		System.out.println("Message received by PokerTableController: " + strMessage);
	}

	private Label getPlayerLabel(int iPosition) {
		switch (iPosition) {
		case 1:
			return lblPlayerPos1;
		case 2:
			return lblPlayerPos2;
		default:
			return null;
		}
	}

	private ToggleButton getSitLeave(int iPosition) {
		switch (iPosition) {
		case 1:
			return btnPos1SitLeave;
		case 2:
			return btnPos2SitLeave;
		default:
			return null;
		}
	}

	private HBox getCardHBox(int iPosition) {
		switch (iPosition) {
		case 0:
			return hboxCommunity;
			
		case 1:
			return hboxP1Cards;
			
		case 2:
			return hboxP2Cards;

		default:
			return null;
		}

	}

	private ImageView BuildImage(int iCardNbr) {
		String strImgPath = null;

		int iWidth = 72;
		int iHeight = 96;
		switch (iCardNbr) {
		case -1:
			strImgPath = "/include/img/b1fh.png";
			break;
		case -2:
			strImgPath = "/include/img/blank.png";
			break;
		case 0:
			strImgPath = "/include/img/b1fv.png";
			break;
		default:
			strImgPath = "/include/img/" + iCardNbr + ".png";
		}

		ImageView i1 = new ImageView(
				new Image(getClass().getResourceAsStream(strImgPath), iWidth, iHeight, true, true));
		return i1;
	}

	@FXML
	void btnStart_Click(ActionEvent event) {

		hboxP1Cards.getChildren().clear();
		hboxP2Cards.getChildren().clear();

		hboxDeck.getChildren().clear();
		hboxDeck.getChildren().add(BuildImage(0));

		for (int a = 0; a < 5; a++) {
			for (int b = 1; b < 3; b++) {
				getCardHBox(b).getChildren().add(BuildImage(-2));
			}
		}

	}

	public void btnDrawCard_Click(ActionEvent event) {
		Point2D pntCardDealt = null;

		SequentialTransition seqDealTable = new SequentialTransition();

		for (int iPosition = 1; iPosition < 3; iPosition++) {

			pntCardDealt = FindPoint(getCardHBox(iPosition), iDrawCard);

			Point2D pntDeck = FindPoint(hboxDeck, 0);

			final ImageView img = BuildImage(0);
			img.setX(pntDeck.getX());
			img.setY(pntDeck.getY() - 33);

			ImageView imgDealCard = BuildImage(11);

			mainAnchor.getChildren().add(img);

			TranslateTransition transT = CreateTranslateTransition(pntDeck, pntCardDealt, img);

			RotateTransition rotT = CreateRotateTransition(img);
			rotT.setCycleCount(6);

			
			ScaleTransition scaleT = CreateScaleTransition(img);
			scaleT.setCycleCount(2);
			scaleT.setAutoReverse(true);
			scaleT.setByX(0.5);
			scaleT.setByY(0.5);
		     
			PathTransition pathT = CreatePathTransition(pntDeck,
			 pntCardDealt, img);
			QuadCurve path = new QuadCurve();
			int pos = iPosition;
			switch (pos) {
			case 1:
				path.setStartX(50);
				path.setStartY(OuterBorderPane.getHeight()/2-50);
				
				path.setControlX(OuterBorderPane.getWidth());
				path.setControlY(OuterBorderPane.getHeight()/2);
				
				path.setEndX(pntCardDealt.getX());
				path.setEndY(pntCardDealt.getY());
				
				break;
			
			case 2:
				path.setStartX(50);
				path.setStartY(OuterBorderPane.getHeight()/2-50);
				
				path.setControlX(OuterBorderPane.getWidth());
				path.setControlY(OuterBorderPane.getHeight()/2);
				
				path.setEndX(pntCardDealt.getX());
				path.setEndY(pntCardDealt.getY());
				break;
			}
			
			pathT.setPath(path);
			
			FadeTransition fadeT = new FadeTransition(Duration.millis(400), img);
			fadeT.setFromValue(1.0);
			fadeT.setToValue(0);
			fadeT.setCycleCount(2);
			fadeT.setDelay(Duration.millis(600));
			
			

			ParallelTransition patTMoveRot = new ParallelTransition(rotT,scaleT, pathT, fadeT);
			// patTMoveRot.getChildren().addAll(rotT, pathT);
			// patTMoveRot.getChildren().addAll(pathT, rotT);

			ParallelTransition patTFadeInFadeOut = createFadeTransition(
					(ImageView) getCardHBox(iPosition).getChildren().get(iDrawCard),
					imgDealCard.getImage());

			SequentialTransition seqDeal = new SequentialTransition();

			seqDeal.getChildren().addAll(patTMoveRot, patTFadeInFadeOut);

			seqDeal.setOnFinished(new EventHandler<ActionEvent>() {

				public void handle(ActionEvent arg0) {
					mainAnchor.getChildren().remove(img);
				}
			});

			seqDealTable.getChildren().add(seqDeal);
		}

		seqDealTable.setInterpolator(Interpolator.EASE_OUT);
		seqDealTable.play();
		iDrawCard++;

	}

	private Point2D FindPoint(HBox hBoxCard, int iCardNbr) {

		ImageView imgvCardFaceDown = (ImageView) hBoxCard.getChildren().get(iCardNbr);
		Bounds bndCardDealt = imgvCardFaceDown.localToScene(imgvCardFaceDown.getBoundsInLocal());
		Point2D pntCardDealt = new Point2D(bndCardDealt.getMinX() + iCardNbr, bndCardDealt.getMinY());

		return pntCardDealt;
	}

	private PathTransition CreatePathTransition(Point2D fromPoint, Point2D toPoint, ImageView img) {
		Path path = new Path();
		path.getElements().add(new MoveTo(fromPoint.getX(), fromPoint.getY()));
		path.getElements().add(new CubicCurveTo(toPoint.getX() * 2, toPoint.getY() * 2, toPoint.getX() / 3,
				toPoint.getY() / 3, toPoint.getX(), toPoint.getY()));
		// path.getElements().add(new CubicCurveTo(0, 120, 0, 240, 380, 240));
		PathTransition pathTransition = new PathTransition();
		pathTransition.setDuration(Duration.millis(750));
		pathTransition.setPath(path);
		pathTransition.setNode(img);
		pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
		pathTransition.setCycleCount((int) 1f);
		pathTransition.setAutoReverse(false);

		return pathTransition;

	}

	private ScaleTransition CreateScaleTransition(ImageView img) {
		ScaleTransition st = new ScaleTransition(Duration.millis(iAnimationLength), img);
		st.setByX(.25f);
		st.setByY(.25f);
		st.setCycleCount((int) 1f);
		st.setAutoReverse(true);

		return st;
	}

	private RotateTransition CreateRotateTransition(ImageView img) {

		RotateTransition rotateTransition = new RotateTransition(Duration.millis(iAnimationLength/2), img);
		rotateTransition.setByAngle(180F);
		rotateTransition.setCycleCount(2);
		rotateTransition.setAutoReverse(false);

		return rotateTransition;
	}

	private TranslateTransition CreateTranslateTransition(Point2D fromPoint, Point2D toPoint, ImageView img) {

		TranslateTransition translateTransition = new TranslateTransition(Duration.millis(iAnimationLength), img);

		translateTransition.setFromX(0);
		translateTransition.setToX(toPoint.getX() - fromPoint.getX());
		translateTransition.setFromY(0);
		translateTransition.setToY(toPoint.getY() - fromPoint.getY());
		translateTransition.setCycleCount(1);
		translateTransition.setAutoReverse(false);

		return translateTransition;
	}

	private ParallelTransition createFadeTransition(final ImageView imgVFadeOut, final Image imgFadeIn) {

		FadeTransition fadeOutTransition = new FadeTransition(Duration.millis(iAnimationLength), imgVFadeOut);
		fadeOutTransition.setFromValue(1.0);
		fadeOutTransition.setToValue(0.0);
		fadeOutTransition.setOnFinished(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent arg0) {
				imgVFadeOut.setImage(imgFadeIn);
			}
		});

		FadeTransition fadeInTransition = new FadeTransition(Duration.millis(iAnimationLength), imgVFadeOut);
		fadeInTransition.setFromValue(0.0);
		fadeInTransition.setToValue(1.0);
		ParallelTransition parallelTransition = new ParallelTransition();
		parallelTransition.getChildren().addAll(fadeOutTransition, fadeInTransition);
		return parallelTransition;
	}

	@FXML
	public void btnFold_Click(ActionEvent event) {

	}

	@FXML
	public void btnCheck_Click(ActionEvent event) {
	}

	private void FadeButton(Button btn) {
		FadeTransition ft = new FadeTransition(Duration.millis(iAnimationLength), btn);
		ft.setFromValue(1.0);
		ft.setToValue(0.3);
		ft.setCycleCount(4);
		ft.setAutoReverse(true);

		ft.play();
	}

}
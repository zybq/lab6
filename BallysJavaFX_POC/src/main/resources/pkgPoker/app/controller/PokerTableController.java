package pkgPoker.app.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import pkgPoker.app.MainApp;
import pkgPokerEnum.eAction;
import pkgPokerEnum.eGame;
import pkgPokerInterfaces.iCardImage;
import pkgPokerBLL.Action;
import pkgPokerBLL.GamePlay;
import pkgPokerBLL.HandScore;
import pkgPokerBLL.Player;
import pkgPokerBLL.Table;

public class PokerTableController implements Initializable {

	// Reference to the main application.
	private MainApp mainApp;

	public PokerTableController() {
	}

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
	private HBox hboxCommunity;

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

	@Override
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

		// Steps:
		// btnSitLeave_Click is executed. Sends a message to:
		// mainApp.messageSend(act)
		// messageSend will send that message to PokerHub
		// PokerHub receives the message and will either do the 'Sit' or 'Leave'
		// action
		// PokerHub will send the message back to all clients
		// mainApp.messageReceived will receive the message from PokerHub
		// mainApp will call HandleTableState
		// HandleTableState will paint the screen

		ToggleButton btn = (ToggleButton) event.getSource();
		eAction eAct = null;

		if (btn.getText().equals("Sit")) {
			eAct = eAction.Sit;
		} else if (btn.getText().equals("Leave")) {
			eAct = eAction.Leave;
		}

		switch (btn.getId()) {

		case "btnPos1SitLeave":
			if (eAct == eAction.Sit) {
				mainApp.getPlayer().setiPlayerPosition(1);
			} else {
				mainApp.getPlayer().setiPlayerPosition(0);
			}
			break;
		case "btnPos2SitLeave":
			if (eAct == eAction.Sit) {
				mainApp.getPlayer().setiPlayerPosition(2);
			} else {
				mainApp.getPlayer().setiPlayerPosition(0);
			}
			break;
		}

		// Build an Action message
		Action act = new Action(eAct, mainApp.getPlayer());

		// Send the Action to the Hub
		mainApp.messageSend(act);
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

	public void Handle_TableState(Table HubPokerTable) {

		// Set default state of the buttons/labels
		getPlayerLabel(1).setText("");
		getPlayerLabel(2).setText("");
		getSitLeave(1).setVisible(true);
		getSitLeave(2).setVisible(true);
		getSitLeave(1).setText("Sit");
		getSitLeave(2).setText("Sit");

		Iterator it = HubPokerTable.getHmPlayer().entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			Player p = (Player) pair.getValue();
			// Set the player label
			getPlayerLabel(p.getiPlayerPosition()).setText(p.getPlayerName());

			// Am I the player
			if (p.getiPokerClientID() == mainApp.getPlayer().getiPokerClientID()) {
				getSitLeave(p.getiPlayerPosition()).setVisible(true);
				getSitLeave(p.getiPlayerPosition()).setText("Leave");

				for (int a = 1; a < 3; a++) {
					if (a != p.getiPlayerPosition())
						getSitLeave(a).setVisible(false);
				}

			}
			// I'm not the player, but someone is sitting in that spot
			else {
				getSitLeave(p.getiPlayerPosition()).setVisible(false);
			}
		}

	}

	public void Handle_GameState(GamePlay HubPokerGame) {

		ArrayList<Integer> iPositions = HubPokerGame.getPlayerPositions();
		
		for (Integer iPos :iPositions)
		{
			ArrayList<iCardImage> iCardImgs = HubPokerGame.GetCardsByPositionPlayer(iPos, mainApp.getPlayer());
			//	Get the cards for each position
			for (iCardImage iCard: iCardImgs)
			{
				ImageView iv = new ImageView(
						new Image(getClass().getResourceAsStream(iCard.ReturnCardImagePath()), 75, 75, true, true));
				getCardHBox(iPos).getChildren().add(iv);
			}
			
			if (getCardHBox(iPos).getChildren().size() > 2)
			{
				HandScore hs = HubPokerGame.GetBestPossibleHand(mainApp.getPlayer());				
				Tooltip.install(
						getCardHBox(iPos),
					    new Tooltip(hs.getHandStrength().toString()));					    
			}
		}
		
		
		
			
	}

	private ImageView BuildImage(int iCardNbr) {
		String strImgPath;
		if (iCardNbr == 0) {
			strImgPath = "/img/b2fv.png";
		} else {
			strImgPath = "/img/" + iCardNbr + ".png";
		}

		ImageView i1 = new ImageView(new Image(getClass().getResourceAsStream(strImgPath), 75, 75, true, true));
		return i1;
	}

	@FXML
	void btnStart_Click(ActionEvent event) {

		hboxP1Cards.getChildren().clear();
		hboxP2Cards.getChildren().clear();

		// Start the Game
		Action act = new Action(eAction.StartGame, mainApp.getPlayer());

		// figure out which game is selected in the menu
		eGame gme = eGame.getGame(Integer.parseInt(mainApp.getRuleName().replace("PokerGame", "")));

		// Set the gme in the action
		act.seteGame(gme);

		// Send the Action to the Hub
		mainApp.messageSend(act);
	}

	@FXML
	/*
	 * void btnDeal_Click(ActionEvent event) {
	 * 
	 * // Set the new Deal action Action act = new Action(eAction.Draw,
	 * mainApp.getPlayer());
	 * 
	 * // Send the Action to the Hub mainApp.messageSend(act);
	 * 
	 * }
	 */

	public void btnDrawCard_Click(ActionEvent event) {
		// Set the new Deal action
		Action act = new Action(eAction.Draw, mainApp.getPlayer());

		// Send the Action to the Hub
		mainApp.messageSend(act);
	}

	@FXML
	public void btnFold_Click(ActionEvent event) {
		Button btnFold = (Button) event.getSource();
		switch (btnFold.getId().toString()) {
		case "btnPlayer1Fold":
			// Fold for Player 1
			break;
		case "btnPlayer2Fold":
			// Fold for Player 2
			break;

		}
	}

	@FXML
	public void btnCheck_Click(ActionEvent event) {
		Button btnCheck = (Button) event.getSource();
		switch (btnCheck.getId().toString()) {
		case "btnPlayer1Check":
			// Check for Player 1
			break;
		case "btnPlayer2Check":
			// Check for Player 2
			break;
		}
	}

	private void FadeButton(Button btn) {
		FadeTransition ft = new FadeTransition(Duration.millis(3000), btn);
		ft.setFromValue(1.0);
		ft.setToValue(0.3);
		ft.setCycleCount(4);
		ft.setAutoReverse(true);

		ft.play();
	}

}
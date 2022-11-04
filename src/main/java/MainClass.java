
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class MainClass {

    public static void main(String[] args) {

        // no need anymore
//        ApiContextInitializer.init();
        try{
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

            telegramBotsApi.registerBot(new KanKor());
			
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
            System.out.println("Bot Started!");
    }
}

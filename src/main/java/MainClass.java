import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MainClass {

    public static void main(String[] args) {

        ApiContextInitializer.init();
		
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
		
        try{
			
            telegramBotsApi.registerBot(new KanKor());
			
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
            System.out.println("Bot Started!");
    }
}

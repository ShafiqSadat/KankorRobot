import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class KanKor extends TelegramLongPollingBot {
    Jedis redis = new Jedis();
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long userID = update.getMessage().getFrom().getId();
            update.getMessage().getFrom().getFirstName();
            update.getMessage().getFrom().getLastName();
            String name = update.getMessage().getFrom().getFirstName();
            int messageID = update.getMessage().getMessageId();
            if (messageText.equals("/start")){
                if (!redis.sismember("KanKorMembers",String.valueOf(userID))){
                    KankorDB.addUser(String.valueOf(userID),name);
                    redis.sadd("KanKorMembers",String.valueOf(userID));
                }
                String text = String.format("سلام %s عزیز به ربات نتایج کانکور خوش آمدید!\nبرای دریافت نتیجه ی کانکور تان روی دکمه دریافت نتیجه کلیک نمائید.",name);

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(userID);
                sendMessage.setText(text);
                sendMessage.setReplyMarkup(sendKeyBoard());
                sendMessage.setReplyToMessageId(messageID);
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

                //deprecated method
//                SendMessage sendMessage = new SendMessage()
//                        .setText(text)
//                        .setChatId(String.valueOf(userID))
//                        .setReplyToMessageId(messageID)
//                        .setReplyMarkup(sendKeyBoard());
//                try {
//                    execute(sendMessage);
//                } catch (TelegramApiException e) {
//                    e.printStackTrace();
//                }
            }
            else if (messageText.equals("ارسال همگانی") && userID == 113566842){
                redis.set("waitForSendAll"+userID,"true");
                sendMessage(userID,"حال متن را ارسال کنید!",messageID,null);
            }
            else if (update.getMessage().hasText() && redis.get("waitForSendAll"+userID)!=null){
                var users = KankorDB.getUsers();
                Thread thread = new Thread(() -> {
                    for (KankorBotModel user : users) {
                        sendMessage(Long.parseLong(user.getUserId()),messageText,0,null);
                    }
                });
                thread.start();
                redis.del("waitForSendAll"+userID);
                sendMessage(userID," به "+users.size()+" کاربر درحال ارسال است...",0,null);
            }
            else if (messageText.equals("یافتن ایدی \uD83C\uDD94")){
                var not = true;
                if (not){
                    sendMessage(userID,"این بخش موقتأ غیر فعال شده است!",messageID,sendKeyBoard());
                    return;
                }
                redis.set("waitForNameForId"+userID,"true");
                sendMessage(userID,"نام تان را ارسال نمائید:\nتوجه داشته باشید که نام تان دقیقا مشابه کارت امتحان تان باشد.",0,sendKeyBoard());
            }
            else if (update.getMessage().hasText() && redis.get("waitForNameForId"+userID)!=null){
                redis.set("userNameForId"+userID,messageText);
                sendMessage(userID,"نام پدر تان را ارسال نمائید:",messageID,sendKeyBoard());
                redis.del("waitForNameForId"+userID);
                redis.set("waitForFatherNameForId"+userID,"true");
            }
            else if (update.getMessage().hasText() && redis.get("waitForFatherNameForId"+userID)!=null){
                redis.set("userFatherNameForId"+userID,messageText);
                sendMessage(userID,"نام پدر کلان تان را ارسال نمائید:",messageID,sendKeyBoard());
                redis.del("waitForFatherNameForId"+userID);
                redis.set("waitForGrandFatherNameForId"+userID,"true");
            }
            else if (update.getMessage().hasText() && redis.get("waitForGrandFatherNameForId"+userID)!=null){
                var nameForID = redis.get("userNameForId"+userID);
                var fatherNameForID = redis.get("userFatherNameForId"+userID);
                    var result = getResultForId(nameForID,fatherNameForID, messageText);
                    sendMessage(userID,result,messageID,sendKeyBoard());
                redis.del("waitForGrandFatherNameForId"+userID);
            }
            else if (messageText.equals("دریافت نتیجه \uD83D\uDCDC")){
                boolean not = false;
                if (not){
                    sendMessage(userID,"کاربر عزیز نتایج به زودی آپلود میشود لطفا صبور باشید.",messageID,null);
                    return;
                }
                redis.set("KanKorWaitForID"+userID,"true");
                sendMessage(userID,"لطفا آیدی کانکور تان را برای دریافت نتیجه ارسال نمائید!",messageID,sendKeyBoard());
            }
            else if (messageText.equals("برنامه نویس \uD83D\uDCBB")){
                String text = "برنامه نویس : سیدشفیق سادات (@Shafiq)\n" +
                        "\n" +
                        "زبان برنامه نویسی : جاوا\n" +
                        "\n" +
                        "نسخه : 0.3";
                sendMessage(userID,text,messageID,sendKeyBoard());
            }
            else if (update.getMessage().hasText()){
//                String kanKorID = messageText;
                Thread thread = new Thread(() -> {
                    sendMessage(userID,"درحال جستجوی نتیجه لطفا منتظر بمانید...",messageID,sendKeyBoard());
                    String result = getResults(toEnglish(messageText));
                        sendMessage(userID,result,messageID,sendKeyBoard());
                });
                thread.start();
            }
//            else if (update.getMessage().hasText()){
//                sendMessage(userID,"دستور ارسالی اشتباه است!\nاگر میخواهید نتیجه کانکور تان را دریافت کنید ابتدا روی دکمه دریافت نتیجه کلیک نمائید سپس آیدی تان را ارسال کنید!",messageID,sendKeyBoard());
//            }
        }
    }

    public void sendMessage(long userID , String messageText,int messageID,ReplyKeyboardMarkup replyKeyboardMarkup){

        SendMessage sendMessage = new SendMessage();
        sendMessage.setReplyToMessageId(messageID);
        sendMessage.setChatId(userID);
        sendMessage.setText(messageText);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        //deprecated method
//        SendMessage sendMessage = new SendMessage()
//                .setChatId(userID)
//                .setReplyMarkup(replyKeyboardMarkup)
//                .setText(messageText)
//                .setReplyToMessageId(messageID);
//        try {
//            execute(sendMessage);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
    }
    String getResults(String id){
        Document doc;
        try {
            Connection.Response res = Jsoup
                    .connect("https://results.nexa.gov.af/fa/kankor1400")
                    .method(Connection.Method.GET)
                    .execute();
            Map<String, String> cookies = res.cookies();
//            System.out.println(res.body());
            var token = Jsoup.parse(res.body());
            doc = Jsoup.connect("https://results.nexa.gov.af/fa/kankor1400")
                    .data("search", id)
                    .data("_token",token.getElementsByTag("input").get(0).val())
                    .data("search_btn","")
                    .cookies(cookies)
                    .userAgent("Mozilla")
                    .post();
//            System.out.println(doc.getElementsByTag("tbody").get(0).getElementsByTag("li").get(0).text());
            if (doc.body().text().contains("!هیچ نتیجه یی با این آی دی نمبر موجود نمی باشد")){
                return "هیچ نتیجه یی با این آی دی نمبر موجود نمی باشد! \n  په دې مشخصاتو هیڅ پايله ونه موندل شوه";
            }
            KankorResponseModel responseModel = new KankorResponseModel();
            responseModel.setKankorId(doc.getElementsByTag("tbody").get(0).getElementsByTag("li").get(0).text());
            responseModel.setName(doc.getElementsByTag("tbody").get(0).getElementsByTag("li").get(1).text());
            responseModel.setFatherName(doc.getElementsByTag("tbody").get(0).getElementsByTag("li").get(2).text());
            responseModel.setGrandFatherName(doc.getElementsByTag("tbody").get(0).getElementsByTag("li").get(3).text());
            responseModel.setSchool(doc.getElementsByTag("tbody").get(0).getElementsByTag("li").get(4).text());
            responseModel.setPoint(doc.getElementsByTag("tbody").get(0).getElementsByTag("li").get(5).text());
            responseModel.setResult(doc.getElementsByTag("tbody").get(0).getElementsByTag("li").get(6).text());
            responseModel.setCity(doc.getElementsByTag("tbody").get(0).getElementsByTag("li").get(7).text());
            responseModel.setSex(doc.getElementsByTag("tbody").get(0).getElementsByTag("li").get(8).text());
            //            System.out.println(result);
            return String.format("آیدی کانکور : %s\nاسم : %s\nاسم پدر : %s\nاسم پدرکلان : %s\nلیسه : %s\nنمره : %s\nنتیجه : %s\nولایت : %s\nجنسیت : %s",responseModel.getKankorId(),responseModel.getName(),responseModel.getFatherName(),responseModel.getGrandFatherName(),responseModel.getSchool(),responseModel.getPoint(),responseModel.getResult(),responseModel.getCity(),responseModel.getSex());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "خطا!\nبه دلیل حجم زیاد درخواست ها ربات فعلا قادر به پاسخگویی نیست، لطفا چند دقیقه بعد مجدد تلاش نمائید...";
    }

    String getResultForId(String name,String faterName,String grangFatherName){
        Document doc;
        try {
            doc = Jsoup.connect("https://nexa.gov.af/search_id.php")
                    .data("nexa_search_name", name)
                    .data("nexa_search_fname", faterName)
                    .data("nexa_search_gname", grangFatherName)
                    .userAgent("Mozilla")
                    .post();
//            if (doc.body().text().contains("آی دی نمبر وارد شده اشتباه میباشد!")){
//                return "آی دی نمبر وارد شده اشتباه میباشد!";
//            }
//            else if (doc.body().text().contains("هیچ نتیجه یی با این آی دی نمبر موجود نمی باشد!")){
//                return "هیچ نتیجه یی با این آی دی نمبر موجود نمی باشد!";
//            }
            if (doc.getElementsByTag("td").get(0).text().contains("هیچ نتیجه یی با این مشخصات دریافت نشد! / په دې مشخصاتو هیڅ پايله ونه موندل شوه")){
                return "هیچ نتیجه یی با این مشخصات دریافت نشد! \n په دې مشخصاتو هیڅ پايله ونه موندل شوه";
            }else{
               var id = doc.getElementsByTag("td").get(0).text().replaceAll("برگشت","").replaceAll(" ","");
                return getResults(id);
            }
        } catch (IOException e) {
//            e.printStackTrace();
        }
        return "خطا!\nبه دلیل حجم زیاد درخواست ها ربات فعلا قادر به پاسخگویی نیست، لطفا چند دقیقه بعد مجدد تلاش نمائید...";
    }
    private ReplyKeyboardMarkup sendKeyBoard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        row.add("دریافت نتیجه \uD83D\uDCDC");
        row.add("یافتن ایدی \uD83C\uDD94");
        row2.add("برنامه نویس \uD83D\uDCBB");
        rows.add(row);
        rows.add(row2);
        replyKeyboardMarkup.setKeyboard(rows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public String toEnglish(String text){
        String[] numbersFA = {"۰","۱","۲","۳","۴","۵","۶","۷","۸","۹"};
        String[] numbersEN = {"0","1","2","3","4","5","6","7","8","9"};
        for (int i = 0; i < numbersFA.length; i++) {
            text.replaceAll(numbersFA[i],numbersEN[i]);
        }
        return text;
    }


    @Override
    public String getBotUsername() {
        return "KankorRobot";
    }

    @Override
    public String getBotToken() {
        return "1131302484:AAHnjaG6bQCXWI_3djLLNZfcJjyQLo12Ef4";
    }
}

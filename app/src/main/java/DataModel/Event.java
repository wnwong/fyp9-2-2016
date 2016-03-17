package DataModel;

import java.util.Calendar;


public class Event {
    private String title;
    private String tradeTime;
    private String tradeDate;
    private String venue;

    public Event(String title, String tradeDate, String tradeTime, String venue) {
        this.title = title;
        this.tradeDate = tradeDate;
        this.tradeTime = tradeTime;
        this.venue = venue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }
}

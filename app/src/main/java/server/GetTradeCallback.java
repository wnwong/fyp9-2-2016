package server;

import java.util.List;

import RealmModel.RealmGadget;

/**
 * Created by User on 22/2/2016.
 */
public interface GetTradeCallback {
    void done(List<RealmGadget> realmGadgets);
}

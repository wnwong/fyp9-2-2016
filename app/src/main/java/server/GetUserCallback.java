package server;


import user.User;

public interface GetUserCallback {
    public abstract void done(User returnedUser);
}

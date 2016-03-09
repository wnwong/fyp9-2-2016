package user;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.secondhandtradingplatform.Login;
import com.example.user.secondhandtradingplatform.R;

import server.GetPostCallback;
import server.ServerRequests;


public class PersonalDetailsFragment extends Fragment implements View.OnClickListener{
    TextView phone, email, location;
    Button chgPwd, confirmBtn;
    TextInputLayout oldPwd, newPwd;
    Boolean pwdCheck, repeatCheck, emptyCheck; // For comparing input old password with actual password
    UserLocalStore userLocalStore;
    ServerRequests serverRequests;
    PersonalDetailsFragmentListener mCallback;
    private static String REQUEST_SUCCESS = "Success";
    private static String TAG = "PersonalDetailsFragment";

    public interface PersonalDetailsFragmentListener{
        void send();
    }
    public PersonalDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        userLocalStore = new UserLocalStore(getContext());
        serverRequests = new ServerRequests(getContext());

        phone = (TextView) view.findViewById(R.id.phone);
        email = (TextView) view.findViewById(R.id.email);
        location = (TextView) view.findViewById(R.id.location);
        oldPwd = (TextInputLayout) view.findViewById(R.id.oldPwd);
        newPwd = (TextInputLayout) view.findViewById(R.id.newPwd);
        chgPwd = (Button) view.findViewById(R.id.chgPwd);
        confirmBtn = (Button) view.findViewById(R.id.confirmBtn);
        chgPwd.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);

        phone.setText(userLocalStore.getLoggedInUser().getPhone());
        email.setText(userLocalStore.getLoggedInUser().getEmail());
        location.setText(userLocalStore.getLoggedInUser().getLocation());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personal_details, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (PersonalDetailsFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement  PersonalDetailsFragmentListener");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chgPwd:
                oldPwd.setVisibility(View.VISIBLE);
                newPwd.setVisibility(View.VISIBLE);
                confirmBtn.setVisibility(View.VISIBLE);
                chgPwd.setVisibility(View.GONE);
                break;
            case R.id.confirmBtn:
                pwdCheck = true;
                emptyCheck = true;
                repeatCheck = true;

                String oldPassword = oldPwd.getEditText().getText().toString();
                String newPassword = newPwd.getEditText().getText().toString();

                if(!oldPassword.equals(userLocalStore.getLoggedInUser().getPassword())){
                    // Check original password
                    pwdCheck = false;
                    Log.i(TAG, "pwdCheck: " + pwdCheck);
                    oldPwd.getEditText().setError("Password not match!!");
                }
                if(newPassword.equals("")){
                    // Check if new password empty
                    emptyCheck = false;
                    Log.i(TAG, "emptyCheck: " + emptyCheck);
                    newPwd.getEditText().setError("This cannot be empty!!");
                }
                if(newPassword.equals(oldPassword)){
                    // Check if new password equals old password
                    repeatCheck =false;
                    Log.i(TAG, "repeatCheck: " + repeatCheck);
                    newPwd.getEditText().setError("You must set a new Password!!");
                }
                if(pwdCheck && emptyCheck && repeatCheck){
                    Log.i(TAG, "New Password: "+newPwd.getEditText().getText().toString());
                    // Connect to server to change pwd
                    changePassword(newPwd.getEditText().getText().toString());
                }
                break;
        }
    }

    private void changePassword(String password){
        serverRequests.ChangePassword(password, userLocalStore.getLoggedInUser().getUser_id(), new GetPostCallback() {
            @Override
            public void done() {

            }
            @Override
            public void done(String response) {
                if(response.contains(REQUEST_SUCCESS)){
                    Log.i(TAG, "REQUEST_SUCCESS");
                  mCallback.send();
                }else{
                    Toast.makeText(getContext(), "Password Change Failed!", Toast.LENGTH_SHORT);
                }
            }
        });
    }
}

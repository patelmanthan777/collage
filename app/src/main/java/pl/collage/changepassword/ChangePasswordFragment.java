package pl.collage.changepassword;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.collage.R;
import pl.collage.base.BaseFragment;
import pl.collage.util.interactors.FirebaseAuthInteractor;

public class ChangePasswordFragment extends BaseFragment implements ChangePasswordView {

    @BindView(R.id.text_input_layout_old_password)
    TextInputLayout oldPasswordTextInputLayout;

    @BindView(R.id.text_input_layout_new_password)
    TextInputLayout newPasswordTextInputLayout;

    @BindView(R.id.text_input_layout_retype_password)
    TextInputLayout retypePasswordTextInputLayout;

    @BindView(R.id.edit_text_old_password)
    TextInputEditText oldPasswordEditText;

    @BindView(R.id.edit_text_new_password)
    TextInputEditText newPasswordEditText;

    @BindView(R.id.edit_text_retype_password)
    TextInputEditText retypePasswordEditText;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.change_password_content_view)
    ViewGroup changePasswordContentView;

    @BindView(R.id.layout_connection_error)
    ViewGroup layoutConnectionError;

    private ChangePasswordPresenter changePasswordPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changePasswordPresenter = new ChangePasswordPresenter(this, new FirebaseAuthInteractor());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.button_change_password)
    public void onChangePasswordButtonClicked() {
        changePasswordPresenter.validatePasswordChange(oldPasswordEditText.getText().toString(),
                newPasswordEditText.getText().toString(), retypePasswordEditText.getText().toString());
    }

    @Override
    public void showProgressBar() {
        closeSoftKeyboard();
        changePasswordContentView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        changePasswordContentView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showPasswordTooShortError() {
        newPasswordTextInputLayout.setError(getString(R.string.password_error));
        retypePasswordTextInputLayout.setError(getString(R.string.password_error));
    }

    @Override
    public void showWrongRetypeError() {
        newPasswordTextInputLayout.setError(getString(R.string.should_match_error));
        retypePasswordTextInputLayout.setError(getString(R.string.should_match_error));
    }

    @Override
    public void showEmptyOldPasswordError() {
        oldPasswordTextInputLayout.setError(getString(R.string.empty_old_password_error));
    }

    @Override
    public void hideEmptyOldPasswordError() {
        oldPasswordTextInputLayout.setError(null);
    }

    @Override
    public void hideNewPasswordErrors() {
        newPasswordTextInputLayout.setError(null);
        retypePasswordTextInputLayout.setError(null);
    }

    @Override
    public void showChangePasswordError() {
        Toast.makeText(getContext(), R.string.change_password_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showChangePasswordSuccess() {
        Toast.makeText(getContext(), R.string.change_password_success, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finishWithSuccess() {
        getActivity().finish();
    }

    @OnClick(R.id.button_retry)
    public void onRetryClicked() {
        changePasswordPresenter.validatePasswordChange(oldPasswordEditText.getText().toString(),
                newPasswordEditText.getText().toString(), retypePasswordEditText.getText().toString());
    }
}

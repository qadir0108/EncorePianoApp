package com.encore.piano.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cooltechworks.creditcarddesign.CardEditActivity;
import com.cooltechworks.creditcarddesign.CreditCardUtils;
import com.cooltechworks.creditcarddesign.CreditCardView;
import com.encore.piano.R;
import com.encore.piano.asynctasks.SendPaymentAsync;
import com.encore.piano.data.StringConstants;
import com.encore.piano.model.AssignmentModel;
import com.encore.piano.server.Service;
import com.encore.piano.util.Alerter;
import com.encore.piano.util.UIUtility;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AssignmentPayment extends AppCompatActivity  implements
        View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    TextView tvAssignmentNumber;
    TextView tvAmount;
    RadioGroup rdgPayVia;
    LinearLayout layoutCheckNumber, layoutCheckDate;
    Button btnPay;
    Button btnCard;
    Button btnSaveCard;
    Button btnCancel;

    AssignmentModel model = null;
    String assignmentId;
    CreditCardView cardView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.assignment_payment);

        assignmentId = getIntent().getExtras().getString(StringConstants.INTENT_KEY_ASSIGNMENT_ID);
        model = Service.assignmentService.getAll(assignmentId);

        tvAssignmentNumber = (TextView) findViewById(R.id.tvAssignmentNumber);
        tvAmount = (TextView) findViewById(R.id.tvAmount);
        rdgPayVia = (RadioGroup)findViewById(R.id.rdgPayVia);
        rdgPayVia.setOnCheckedChangeListener(this);
        layoutCheckNumber = (LinearLayout)findViewById(R.id.layoutCheckNumber);
        layoutCheckDate = (LinearLayout)findViewById(R.id.layoutCheckDate);
        btnPay =(Button)findViewById(R.id.btnPay);
        btnPay.setOnClickListener(this);
        btnCard =(Button)findViewById(R.id.btnCard);
        btnCard.setOnClickListener(this);
        btnSaveCard =(Button)findViewById(R.id.btnSaveCard);
        btnSaveCard.setOnClickListener(this);
        btnPay =(Button)findViewById(R.id.btnPay);
        btnPay.setOnClickListener(this);
        btnCancel =(Button)findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);
        tvAssignmentNumber.setText(model.getAssignmentNumber());
        tvAmount.setText("$" + model.getPaymentAmount());
        cardView = (CreditCardView) findViewById(R.id.cardView);
        if("".equals(cardView.getCardHolderName())) {
            btnCard.setText("New Card");
        } else {
            btnCard.setText("Change Card");
        }
    }

    public void onActivityResult(int reqCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK) {

            String cardHolderName = data.getStringExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME);
            String cardNumber = data.getStringExtra(CreditCardUtils.EXTRA_CARD_NUMBER);
            String expiry = data.getStringExtra(CreditCardUtils.EXTRA_CARD_EXPIRY);
            String cvv = data.getStringExtra(CreditCardUtils.EXTRA_CARD_CVV);

            // Your processing goes here.
            cardView.setCardHolderName(cardHolderName);
            cardView.setCardNumber(cardNumber);
            cardView.setCardExpiry(expiry);
            cardView.setCVV(cvv);

            btnCard.setText("Change Card");
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.btnCard:
                if("".equals(cardView.getCardHolderName())) {
                    // New
                    final int GET_NEW_CARD = 2;
                    Intent intent = new Intent(AssignmentPayment.this, CardEditActivity.class);
                    startActivityForResult(intent,GET_NEW_CARD);
                } else {
                    // Edit
                    final int EDIT_CARD = 5;
                    Intent intent = new Intent(AssignmentPayment.this, CardEditActivity.class);
                    intent.putExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME, cardView.getCardHolderName());
                    intent.putExtra(CreditCardUtils.EXTRA_CARD_NUMBER, cardView.getCardNumber());
                    intent.putExtra(CreditCardUtils.EXTRA_CARD_EXPIRY, cardView.getExpiry());
                    intent.putExtra(CreditCardUtils.EXTRA_CARD_CVV, cardView.getCVV());
                    //intent.putExtra(CreditCardUtils.EXTRA_CARD_SHOW_CARD_SIDE, CreditCardUtils.CARD_SIDE_BACK);
                    intent.putExtra(CreditCardUtils.EXTRA_VALIDATE_EXPIRY_DATE, true); // pass "false" to discard expiry date validation.
                    startActivityForResult(intent, EDIT_CARD);
                }
                break;
            case R.id.btnPay:

                new SweetAlertDialog(AssignmentPayment.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(Alerter.title)
                        .setContentText("Are you sure?")
                        .setConfirmText("Yes,Save")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                            sDialog.hide();
                            // Payment
                            String[] expiry = cardView.getExpiry().split("/");
                            Card card = new Card(cardView.getCardNumber(), new Integer(expiry[0]), new Integer("20" + expiry[1]), cardView.getCVV());
                            card.setName(cardView.getCardHolderName());
                            Stripe stripe = new Stripe(AssignmentPayment.this, "pk_test_2Fb8LJCuSwSKoZB4ppygLvI6");
                            btnPay.setEnabled(false);
                            stripe.createToken(
                                    card,
                                    new TokenCallback() {
                                        public void onSuccess(Token token) {
                                            new SendPaymentAsync(AssignmentPayment.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, model.getId(), token.getId());
                                            btnPay.setEnabled(true);
                                        }
                                        public void onError(Exception error) {
                                            btnPay.setEnabled(true);
                                            Alerter.error(AssignmentPayment.this, error.getLocalizedMessage());
                                        }
                                    }
                            );

                            }
                        })
                        .setCancelText("No, cancel!")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .show();
                break;

            case R.id.btnCancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
            default:
                break;
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.main:
			this.finish();
			Intent i = new Intent(this, StartScreen.class);
			i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(i);
			break;
		}

		return super.onOptionsItemSelected(item);
	}

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        // This will get the radiobutton that has changed in its check state
        RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
        // This puts the value (true/false) into the variable
        boolean isChecked = checkedRadioButton.isChecked();
        // If the radiobutton that has changed in check state is now checked...

        if (UIUtility.isCheckedRadio(this, R.id.rdPayViaCreditCard))
        {
            cardView.setVisibility(View.VISIBLE);
            btnCard.setVisibility(View.VISIBLE);
            layoutCheckNumber.setVisibility(View.GONE);
            layoutCheckDate.setVisibility(View.GONE);
        } else if (UIUtility.isCheckedRadio(this, R.id.rdPayViaCash))
        {
            cardView.setVisibility(View.GONE);
            btnCard.setVisibility(View.GONE);
            layoutCheckNumber.setVisibility(View.GONE);
            layoutCheckDate.setVisibility(View.GONE);
        }
        else if (UIUtility.isCheckedRadio(this, R.id.rdPayViaCheck))
        {
            cardView.setVisibility(View.GONE);
            btnCard.setVisibility(View.GONE);
            layoutCheckNumber.setVisibility(View.VISIBLE);
            layoutCheckDate.setVisibility(View.VISIBLE);
        }
    }
}

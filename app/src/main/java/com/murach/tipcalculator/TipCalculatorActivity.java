package com.murach.tipcalculator;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class TipCalculatorActivity extends Activity 
implements OnEditorActionListener, OnClickListener {

    // define variables for the widgets
    private EditText etMortgageAmount;
    private EditText etLength;
    private EditText etAnnInterRate;

    private Button   btnCalculate;
    //private Button   percentDownButton;

    private TextView tvMonthlyInterest;
    private TextView tvTerm;
    private TextView tvMonthlyPayment;
    private TextView tvTotalPayback;
    
    // define the SharedPreferences object
    private SharedPreferences savedValues;
    
    // define instance variables that should be saved
    private int mortgageAmount = 0;
    private int mortLength = 0;
    private double annInterRate = 0.0f;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_calculator);
        
        // get references to the widgets
        etMortgageAmount = (EditText) findViewById(R.id.etMortgageAmount);
        etLength = (EditText) findViewById(R.id.etLength);
        etAnnInterRate = (EditText) findViewById(R.id.etAnnInterestRate);

        tvMonthlyInterest = (TextView) findViewById(R.id.tvMonthlyInterestRate);
        tvTerm = (TextView) findViewById(R.id.tvTerm);
        tvMonthlyPayment = (TextView) findViewById(R.id.tvMonthlyPayment);
        tvTotalPayback = (TextView) findViewById(R.id.tvTotalPayback);

        btnCalculate = (Button) findViewById(R.id.btnCalculate);

        // set the listeners
        btnCalculate.setOnClickListener(this);

        // get SharedPreferences object
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }
    /*
    @Override
    public void onPause() {
        // save the instance variables       
        Editor editor = savedValues.edit();        
        editor.putString("billAmountString", billAmountString);
        editor.putFloat("tipPercent", tipPercent);
        editor.commit();        

        super.onPause();      
    }
    
    @Override
    public void onResume() {
        super.onResume();
        
        // get the instance variables
        billAmountString = savedValues.getString("billAmountString", "");
        tipPercent = savedValues.getFloat("tipPercent", 0.15f);

        // set the bill amount on its widget
        mortgageAmountEditText.setText(billAmountString);
        
        // calculate and display
        calculateAndDisplay();
    }    
    */
    public void calculateAndDisplay() {
        double monthIntRate;
        double term;
        double monthPay;
        double totalPayBack;
        int numPayments;

        //Get Mortgage loan amount, length in years, and annual interest rate
        String mortgageAmountString = etMortgageAmount.getText().toString();
        mortgageAmount = Integer.parseInt(mortgageAmountString);

        String mortLengthString = etLength.getText().toString();
        mortLength = Integer.parseInt(mortLengthString);

        String annualIntRateString = etAnnInterRate.getText().toString();
        annInterRate = (Float.parseFloat(annualIntRateString))/100;

        monthIntRate = annInterRate/12;
        numPayments = mortLength * 12;
        term = Math.pow((1+monthIntRate),numPayments);
        monthPay = (mortgageAmount * monthIntRate * term)/(term-1);
        totalPayBack = monthPay * numPayments;

        NumberFormat percent = NumberFormat.getPercentInstance();
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        NumberFormat format = NumberFormat.getInstance();

        tvMonthlyInterest.setText(percent.format(monthIntRate));

        tvTerm.setText(format.format(term));

        tvMonthlyPayment.setText(currency.format(monthPay));

        tvTotalPayback.setText(currency.format(totalPayBack));

    }
    
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
            actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            calculateAndDisplay();
        }        
        return false;
    }
    
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnCalculate ){
            calculateAndDisplay();
        }
    }
}
package com.example.minicalculdora;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {
    //objetos de formatação da MOEDA e da PORCENTAGEM
    private static NumberFormat currecyFormat = NumberFormat.getCurrencyInstance();
    private static NumberFormat percentFormat = NumberFormat.getPercentInstance();
    //variáveis para a lógica de aplicação
    private double billAmount = 0.0;
    private double percent = 0.15;
    //variavéis com as referências aos elementos da interface gráfica
    private TextView amountTextView;
    private TextView percentTextView;
    private TextView tipTextView;
    private TextView totalTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //configurando as referências as TextViews da tela
        amountTextView = findViewById(R.id.amountTextView);
        percentTextView = findViewById(R.id.percentTextView);
        tipTextView = findViewById(R.id.tipTextView);
        totalTextView = findViewById(R.id.totalTextView);

        //configura os valores iniciais da taxa e total
        tipTextView.setText(currecyFormat.format(0));//R$ 0,00 U$ 0.00
        totalTextView.setText(currecyFormat.format(0));

        //configura o tratamento de eventos para a caixa de texto
        EditText amountEditText = findViewById(R.id.amountEditText);
        amountEditText.addTextChangedListener(amountEditTextWatcher);

        //configura o tratamento de eventos para a barra seekBar
        SeekBar percentSeekBar = findViewById(R.id.percentSeekBar);
        percentSeekBar.setOnSeekBarChangeListener(seekBarListener);
    }
    private void calculate(){
        //calcula a gorjeta e o total final da conta
        double tip = billAmount * percent;
        double total = billAmount + tip;
        //mostra os resultados formatados no padrão da moeda
        tipTextView.setText(currecyFormat.format(tip));
        totalTextView.setText(currecyFormat.format(total));
    }

    private final SeekBar.OnSeekBarChangeListener seekBarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            //atualiza o valor da porcentagem com o novo valor selecionado
            percent = i/100.0;
            //mostrar o valor da porcentagem atualizado na tela
            percentTextView.setText(percentFormat.format(percent));
            //manda (re)calcular os valores
            calculate();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    };

    private final TextWatcher amountEditTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            try{
                //faz a leitura do valor da caixa de texto e converte em número real (double)
                billAmount = Double.parseDouble(charSequence.toString())/100.0;
                amountTextView.setText(currecyFormat.format(billAmount));
            }
            catch (NumberFormatException e) {
                //se a entrada for um valor em branco (o usuário apagou o que digitou por exemplo)
                //dispara uma exceção. Então o programa seta o valor da conta como ZERO
                billAmount = 0.0;
                //reseta a entrada com a mensagem de dica inicial
                amountTextView.setText(R.string.enter_amount);
            }
            //manda (re)calcular os valores
            calculate();
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable) {}
    };
}
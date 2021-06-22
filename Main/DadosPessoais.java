package com.example.agenda;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.agenda.DAO.DAO;
import com.example.agenda.objetos.Pessoa;

public class DadosPessoais extends AppCompatActivity {

    ImageView activity_dados_pessoais_icone;
    EditText activity_dados_pessoais_editText_nome_recebido,
            activity_dados_pessoais_editText_telefone_recebido;
    Spinner activity_dados_pessoais_spinner_sexo_recebido;

    Button  activity_dados_pessoais_button_atualizar,
            activity_dados_pessoais_button_excluir,
            activity_dados_pessoais_button_voltar;

    String activity_dados_pessoais_nome_recebido;

    String activity_dados_pessoais__sexo;

    String ultimoCaracterDigitado = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_pessoais);

        activity_dados_pessoais_icone = findViewById(R.id.activity_dados_pessoais_icone);
        activity_dados_pessoais_editText_nome_recebido = findViewById(R.id.activity_dados_pessoais_editText_nome_recebido);
        activity_dados_pessoais_editText_telefone_recebido = findViewById(R.id.activity_dados_pessoais_editText_telefone_recebido);
        activity_dados_pessoais_spinner_sexo_recebido = findViewById(R.id.activity_dados_pessoais_spinner_sexo_recebido);

        activity_dados_pessoais_button_atualizar = findViewById(R.id.activity_dados_pessoais_button_atualizar);
        activity_dados_pessoais_button_excluir = findViewById(R.id.activity_dados_pessoais_button_excluir);
        activity_dados_pessoais_button_voltar = findViewById(R.id.activity_dados_pessoais_button_voltar);

        Intent intent = getIntent();


        if(intent.getStringExtra("sexo").equals("F")){
            activity_dados_pessoais_icone.setImageResource(R.drawable.ic_woman);
            activity_dados_pessoais_spinner_sexo_recebido.setSelection(1);
            activity_dados_pessoais__sexo = "F";
        }else{
            activity_dados_pessoais_icone.setImageResource(R.drawable.ic_man);
            activity_dados_pessoais_spinner_sexo_recebido.setSelection(0);
            activity_dados_pessoais__sexo = "M";
        }

        activity_dados_pessoais_spinner_sexo_recebido.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(activity_dados_pessoais_spinner_sexo_recebido.getSelectedItem().equals("Masculino")){
                    activity_dados_pessoais__sexo = "M";
                }else{
                    activity_dados_pessoais__sexo = "F";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        activity_dados_pessoais_nome_recebido = intent.getStringExtra("nome");

        activity_dados_pessoais_editText_nome_recebido.setText(intent.getStringExtra("nome"));
        activity_dados_pessoais_editText_telefone_recebido.setText(intent.getStringExtra("telefone"));
        //activity_dados_pessoais_spinner_sexo_recebido.setText(intent.getStringExtra("sexo"));

        activity_dados_pessoais_editText_telefone_recebido.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Integer tamanhoEditTelefone = activity_dados_pessoais_editText_telefone_recebido.getText().toString().length();
                if(tamanhoEditTelefone > 1){
                    ultimoCaracterDigitado = activity_dados_pessoais_editText_telefone_recebido.getText().toString().substring(tamanhoEditTelefone-1);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Integer tamanhoEditTelefone = activity_dados_pessoais_editText_telefone_recebido.getText().toString().length();
                if(tamanhoEditTelefone == 2){
                    if(!ultimoCaracterDigitado.equals(" ")){
                        activity_dados_pessoais_editText_telefone_recebido.append(" ");
                    }else{
                        activity_dados_pessoais_editText_telefone_recebido.getText().delete(tamanhoEditTelefone -1, tamanhoEditTelefone);
                    }
                }else if(tamanhoEditTelefone == 8){
                    if(!ultimoCaracterDigitado.equals("-")){
                        activity_dados_pessoais_editText_telefone_recebido.append("-");
                    }else{
                        activity_dados_pessoais_editText_telefone_recebido.getText().delete(tamanhoEditTelefone -1, tamanhoEditTelefone);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activity_dados_pessoais_button_atualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizarPessoa();
            }
        });

        activity_dados_pessoais_button_excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder confirmacaoExclusao = new AlertDialog.Builder(DadosPessoais.this);
                confirmacaoExclusao.setTitle("Atencao!");
                confirmacaoExclusao.setMessage("Tem certeza que deseja apagar " +
                        activity_dados_pessoais_nome_recebido + " ?");
                confirmacaoExclusao.setCancelable(false);
                confirmacaoExclusao.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        apagaPessoa();
                    }
                });
                confirmacaoExclusao.setNegativeButton("Nao", null);
                confirmacaoExclusao.create().show();
            }
        });

        activity_dados_pessoais_button_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void apagaPessoa() {
        DAO dao = new DAO(getApplicationContext());
        dao.apagarPessoa(activity_dados_pessoais_nome_recebido);
        finish();
    }

    private void atualizarPessoa() {
        DAO dao = new DAO(getApplicationContext());
        Pessoa pessoaParaAtualizar = new Pessoa();
        pessoaParaAtualizar.setNome(activity_dados_pessoais_editText_nome_recebido.getText().toString());
        pessoaParaAtualizar.setSexo(activity_dados_pessoais__sexo);
        pessoaParaAtualizar.setTelefone(activity_dados_pessoais_editText_telefone_recebido.getText().toString());

        dao.inserirPessoa(pessoaParaAtualizar, activity_dados_pessoais_nome_recebido);
        dao.close();
        finish();
    }
}

package com.example.agenda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.agenda.DAO.DAO;
import com.example.agenda.adapter.RecyclerViewAdapter;
import com.example.agenda.objetos.Pessoa;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editTextNome, editTextTelefone;
    Switch switchSexo;
    Button buttonSalvar;

    Context context;
    RecyclerView recyclerView;

    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;

    String ultimoCaracterDigitado = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        editTextNome = (EditText) findViewById(R.id.editTextNome);
        editTextTelefone = (EditText) findViewById(R.id.editTextTelefone);
        switchSexo = (Switch) findViewById(R.id.switchSexo);
        buttonSalvar = (Button) findViewById(R.id.buttonSalvar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        chamaLista();

        editTextTelefone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Integer tamanhoEditTelefone = editTextTelefone.getText().toString().length();
                if(tamanhoEditTelefone > 1){
                    ultimoCaracterDigitado = editTextTelefone.getText().toString().substring(tamanhoEditTelefone-1);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Integer tamanhoEditTelefone = editTextTelefone.getText().toString().length();
                if(tamanhoEditTelefone == 2){
                    if(!ultimoCaracterDigitado.equals(" ")){
                        editTextTelefone.append(" ");
                    }else{
                        editTextTelefone.getText().delete(tamanhoEditTelefone -1, tamanhoEditTelefone);
                    }
                }else if(tamanhoEditTelefone == 8){
                    if(!ultimoCaracterDigitado.equals("-")){
                        editTextTelefone.append("-");
                    }else{
                        editTextTelefone.getText().delete(tamanhoEditTelefone -1, tamanhoEditTelefone);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sexo;

                if(switchSexo.isChecked()){
                    sexo = "F";
                }else{
                    sexo = "M";
                }


                if(!(editTextNome.getText().toString().equals("") ||
                        editTextTelefone.getText().toString().equals(""))){
                    inserePessoa(sexo);
                    limparCampos();
                    chamaLista();

                    Toast.makeText(getApplicationContext(),"Inserido!",
                            Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Preencha tudo!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void inserePessoa(String sexo) {
        DAO dao = new DAO(getApplicationContext());
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(editTextNome.getText().toString());
        pessoa.setSexo(sexo);
        pessoa.setTelefone(editTextTelefone.getText().toString());

        dao.inserirPessoa(pessoa,null);
        dao.close();
    }

    private void chamaLista() {
        DAO dao1 = new DAO(getApplicationContext());

        List<Pessoa> pessoas = dao1.buscarPessoa();

        List<String> nomes = new ArrayList<String>();
        List<String> telefones = new ArrayList<String>();
        List<String> sexos  = new ArrayList<String>();

        String[] dados_nomes = new String[]{};
        String[] dados_telefones = new String[]{};
        String[] dados_sexos = new String[]{};

        for(Pessoa busca:pessoas){
            nomes.add(busca.getNome());
            telefones.add(String.valueOf(busca.getTelefone()));
            sexos.add(String.valueOf(busca.getSexo()));
        }

        dados_nomes = nomes.toArray(new String[0]);
        dados_telefones = telefones.toArray(new String[0]);
        dados_sexos = sexos.toArray(new String[0]);

        recyclerViewLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerViewAdapter = new RecyclerViewAdapter(context, dados_nomes, dados_telefones, dados_sexos);
        recyclerView.setAdapter(recyclerViewAdapter);

    }

    private void limparCampos() {
        editTextNome.setText("");
        editTextNome.requestFocus();
        editTextTelefone.setText("");
        switchSexo.setChecked(false);
    }

    // Sobrescricao do metodo chamaLista();
    @Override
    public void onResume(){
        super.onResume();
        chamaLista();
    }
}
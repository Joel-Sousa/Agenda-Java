package com.example.agenda.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.agenda.objetos.Pessoa;

import java.util.ArrayList;
import java.util.List;

public class DAO extends SQLiteOpenHelper {

    public DAO(Context context){
        super(context, "banco", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String sql = "CREATE TABLE pessoa( nome TEXT UNIQUE, sexo TEXT, telefone String);";
        db.execSQL(sql);
        //db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        String sql = "DROP TABLE IF EXISTS pessoa;";
        db.execSQL(sql);
        onCreate(db);
    }

    public void inserirPessoa(Pessoa pessoa,String pessoaParaAtualizar){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = new ContentValues();

        dados.put("sexo", pessoa.getSexo());
        dados.put("telefone", pessoa.getTelefone());

        if(pessoaParaAtualizar != null){
            dados.put("nome", pessoaParaAtualizar);
        }else{
            dados.put("nome", pessoa.getNome());
        }

//        Log.d("Recebeu:",pessoaParaAtualizar);
//        Log.d("Nome do objeto:",pessoa.getNome());

        try{
            db.insertOrThrow("pessoa", null, dados);
            //db.insert("pessoa", null, dados);

//            Log.d("Executol","o try");

        }catch(SQLiteConstraintException e){
            dados.put("nome", pessoa.getNome());
            db.update("pessoa", dados, "nome = ?", new String[]{pessoaParaAtualizar});
//            Log.d("Executou","o catch");
        }
    }

    public List<Pessoa> buscarPessoa(){

        SQLiteDatabase  db = getReadableDatabase();
        String sql = "SELECT * FROM pessoa;";
        Cursor c = db.rawQuery(sql, null);
        List<Pessoa> pessoas = new ArrayList<Pessoa>();

        while(c.moveToNext()){
            Pessoa pessoa = new Pessoa();
            pessoa.setNome(c.getString(c.getColumnIndex("nome")));
            pessoa.setSexo(c.getString(c.getColumnIndex("sexo")));
            pessoa.setTelefone(c.getString(c.getColumnIndex("telefone")));
            pessoas.add(pessoa);
        }
        return pessoas;
    }

    public void apagarPessoa(String nome){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DELETE FROM pessoa WHERE nome = " + "'"+nome+"';";
        db.execSQL(sql);
    }

}

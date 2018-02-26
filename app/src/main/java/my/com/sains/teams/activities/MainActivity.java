package my.com.sains.teams.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import org.greenrobot.greendao.query.Query;

import java.util.List;

import my.com.sains.teams.R;
import my.com.sains.teams.db.DaoSession;
import my.com.sains.teams.db.User;
import my.com.sains.teams.db.UserDao;

public class MainActivity extends AppCompatActivity {

    private UserDao userDao;
    private Query<User> userQuery;
    private List<User> userList;
    EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.edit_text);
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        userDao = daoSession.getUserDao();

//        userQuery = userDao.queryBuilder().orderAsc(UserDao.Properties.First_name).build();
//        userList = userQuery.list();



    }
//
//    public void add(View v){
//
//        String addText = editText.getText().toString();
//
//        User user = new User();
//        user.setFirst_name(addText);
//        userDao.insert(user);
//    }
//
//    public void delete(View v){
//
//        String text = editText.getText().toString();
//        List<User> users = userDao.queryBuilder()
//                .where(UserDao.Properties.First_name.eq(text))
//                .list();
//        for(User user: users){
//            userDao.delete(user);
//        }
//    }
//
//    public void update(View v){
//
//        String text = editText.getText().toString();
//
//        List<User> users = userDao.queryBuilder()
//                .where(UserDao.Properties.First_name.eq(text))
//                .list();
//
//        for(User user: users){
//            user.setFirst_name(user.getFirst_name()+ ": Updated");
//            userDao.update(user);
//        }
//
//
//    }
//
//    public void refresh(View v){
//        userQuery = userDao.queryBuilder().orderAsc(UserDao.Properties.First_name).build();
//        userList = userQuery.list();
//
//        for (User user: userList){
//
//            Log.e("user", user.getFirst_name());
//        }
//    }
}

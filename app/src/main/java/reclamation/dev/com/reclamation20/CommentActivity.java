package reclamation.dev.com.reclamation20;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CommentActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Bundle bundle= getIntent().getExtras();

        ViewCommentsFragment fragment = new ViewCommentsFragment();
        Bundle b= new Bundle();


        b.putString("userid",bundle.getString("userid"));
        b.putString("idpost",bundle.getString("idpost"));

        fragment.setArguments(b);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(getString(R.string.comment_fragment));
        transaction.commit();
    }
}

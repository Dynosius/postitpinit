package projects.riteh.post_itpin_it.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class Repository {

    private PostDAO mPostDao;
    private LiveData<List<Post>> mAllPosts;

    public Repository(Application application){
        PostDatabase db = PostDatabase.getAppDatabase(application);
        mPostDao = db.postDAO();
        mAllPosts = mPostDao.getAll();
    }

    public LiveData<List<Post>> getmAllPosts(){
        return mAllPosts;
    }

    public void insert(Post post)
    {
        new insertAsyncTask(mPostDao).execute(post);
    }
    public void delete(Post post) { new deleteAsyncTask(mPostDao).execute(post); }
    public void update(Post post) { new updateAsyncTask(mPostDao).execute(post); }

    // For multithreading/asynchronous work
    private static class insertAsyncTask extends AsyncTask<Post, Void, Void> {
        private PostDAO mAsyncTaskDao;

        insertAsyncTask(PostDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Post... params) {
            mAsyncTaskDao.insertAll(params);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Post, Void, Void> {
        private PostDAO mAsyncTaskDao;

        deleteAsyncTask(PostDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Post... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }
    private static class updateAsyncTask extends AsyncTask<Post, Void, Void> {
        private PostDAO mAsyncTaskDao;

        updateAsyncTask(PostDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Post... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }
}

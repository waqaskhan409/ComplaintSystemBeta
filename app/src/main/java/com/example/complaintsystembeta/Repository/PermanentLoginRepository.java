package com.example.complaintsystembeta.Repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.complaintsystembeta.Database.PermanentLoginDatabase;
import com.example.complaintsystembeta.interfaace.PermanentLoginDao;
import com.example.complaintsystembeta.model.PermanentLogin;

import java.util.List;

public class PermanentLoginRepository {
    private PermanentLoginDao permanentLoginDao;
    private Boolean isLoggedIn;
    public List<PermanentLogin> allRecords;
    public PermanentLoginRepository(Application application){
        PermanentLoginDatabase database = PermanentLoginDatabase.getInstance(application);
        permanentLoginDao = database.permanentLoginDao();
        allRecords = permanentLoginDao.fetchData();
    }

    public void insert(PermanentLogin permanentLogin){
        new InsertPermanentLoginAsyncTask(permanentLoginDao).execute(permanentLogin);

    }
    public void update(PermanentLogin permanentLogin){
        new UpdatePermanentLoginAsyncTask(permanentLoginDao).execute(permanentLogin);

    }
    public void delete(PermanentLogin permanentLogin){
        new DeletePermanentLoginAsyncTask(permanentLoginDao).execute(permanentLogin);
    }

    public List<PermanentLogin> getIsLoggedIn(){
//        new RetrievePermanentLoginAsyncTask(permanentLoginDao).execute();
        return allRecords;
    }
    public void updateUser(PermanentLogin permanentLogin){
        permanentLoginDao.updateUser(permanentLogin.getCNIC(), permanentLogin.getLoggedIn());
//        new UpdateUserPermanentLoginAsyncTask(permanentLoginDao).execute(permanentLogin);
    }
    private static class InsertPermanentLoginAsyncTask extends AsyncTask<PermanentLogin, Void, Void>{
        private PermanentLoginDao permanentLoginDao;

        public InsertPermanentLoginAsyncTask(PermanentLoginDao permanentLoginDao) {
            this.permanentLoginDao = permanentLoginDao;
        }
        @Override
        protected Void doInBackground(PermanentLogin... permanentLogins) {

            permanentLoginDao.insert(permanentLogins[0]);
            return null;
        }
    }

    private static class UpdatePermanentLoginAsyncTask extends AsyncTask<PermanentLogin, Void, Void>{
        private PermanentLoginDao permanentLoginDao;

        public UpdatePermanentLoginAsyncTask(PermanentLoginDao permanentLoginDao) {
            this.permanentLoginDao = permanentLoginDao;
        }

        @Override
        protected Void doInBackground(PermanentLogin... permanentLogins) {

            permanentLoginDao.update(permanentLogins[0]);
            return null;
        }
    }
    private static class DeletePermanentLoginAsyncTask extends AsyncTask<PermanentLogin, Void, Void>{
        private PermanentLoginDao permanentLoginDao;

        public DeletePermanentLoginAsyncTask(PermanentLoginDao permanentLoginDao) {
            this.permanentLoginDao = permanentLoginDao;
        }
        @Override
        protected Void doInBackground(PermanentLogin... permanentLogins) {
            permanentLoginDao.delete(permanentLogins[0]);
            return null;
        }
    }
    private static class UpdateUserPermanentLoginAsyncTask extends AsyncTask<PermanentLogin, Void, Void>{
        private PermanentLoginDao permanentLoginDao;
        public UpdateUserPermanentLoginAsyncTask(PermanentLoginDao permanentLoginDao) {
            this.permanentLoginDao = permanentLoginDao;
        }

        @Override
        protected Void doInBackground(PermanentLogin... permanentLogins) {
            permanentLoginDao.updateUser(permanentLogins[0].getCNIC(), permanentLogins[0].getLoggedIn());
            return null;
        }
    }
}

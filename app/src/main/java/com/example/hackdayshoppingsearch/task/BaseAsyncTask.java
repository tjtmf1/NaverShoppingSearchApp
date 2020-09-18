package com.example.hackdayshoppingsearch.task;

import android.os.AsyncTask;

public abstract class BaseAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    private PreProcess preProcess;
    private PostProcess<Result> postProcess;

    public void setPostProcess(PostProcess<Result> postProcess) {
        this.postProcess = postProcess;
    }

    public void setPreProcess(PreProcess preProcess) {
        this.preProcess = preProcess;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(preProcess != null) {
            preProcess.onPreProcess();
        }
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        if(postProcess != null) {
            postProcess.onPostProcess(result);
        }
    }

    public interface PreProcess {
        void onPreProcess();
    }

    public interface PostProcess<Result> {
        void onPostProcess(Result result);
    }
}

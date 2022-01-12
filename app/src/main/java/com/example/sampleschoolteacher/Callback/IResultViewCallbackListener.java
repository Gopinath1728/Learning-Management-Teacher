package com.example.sampleschoolteacher.Callback;



import com.example.sampleschoolteacher.Model.ResultModel;

import java.util.List;

public interface IResultViewCallbackListener {
    void onResultLoadSuccess(List<ResultModel> resultModelList);
    void onResultLoadFailed(String resultLoadError);
}

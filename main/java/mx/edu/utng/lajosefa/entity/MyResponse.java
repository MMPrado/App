package mx.edu.utng.lajosefa.entity;

import java.util.List;

/**
 * Created by Diana Manzano on 29/03/2018.
 */

public class MyResponse{
    public long multicastId;
    public int success;
    public int failure;
    public int canonicalIds;
    public List<Result> results;

    public MyResponse(long multicastId, int success, int failure, int canonicalIds, List<Result> results) {
        this.multicastId = multicastId;
        this.success = success;
        this.failure = failure;
        this.canonicalIds = canonicalIds;
        this.results = results;
    }

    public MyResponse() {
    }

    public long getMulticastId() {
        return multicastId;
    }

    public void setMulticastId(long multicastId) {
        this.multicastId = multicastId;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getFailure() {
        return failure;
    }

    public void setFailure(int failure) {
        this.failure = failure;
    }

    public int getCanonicalIds() {
        return canonicalIds;
    }

    public void setCanonicalIds(int canonicalIds) {
        this.canonicalIds = canonicalIds;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }
}

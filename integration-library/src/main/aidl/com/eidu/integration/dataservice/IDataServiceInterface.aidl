package com.eidu.integration.dataservice;

interface IDataServiceInterface {
    void recordEvent(String id, String segmentation, long time, long duration);
}

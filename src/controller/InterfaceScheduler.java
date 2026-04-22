package controller;

import model.ProcessQueue;
public interface InterfaceScheduler {

    void executeStep(ProcessQueue queue);

}

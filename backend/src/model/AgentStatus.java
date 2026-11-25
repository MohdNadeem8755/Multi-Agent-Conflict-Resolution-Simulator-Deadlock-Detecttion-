package model;

public enum AgentStatus {

    READY,      // Agent is created and ready to request resources

    WAITING,    // Agent is waiting for a resource

    RUNNING,    // Agent is currently executing

    BLOCKED,    // Agent is blocked due to dependency or deadlock

    FINISHED    // Agent has completed execution

}
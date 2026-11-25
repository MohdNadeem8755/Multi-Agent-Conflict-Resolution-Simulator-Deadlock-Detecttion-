package scheduler;

import model.Conflict;

public interface Scheduler {

    /**
     * Selects the winning agent from a conflict.
     *
     * @param conflict The conflict containing all competing agents.
     * @return The ID of the winning agent.
     */
    int selectWinner(Conflict conflict);

}
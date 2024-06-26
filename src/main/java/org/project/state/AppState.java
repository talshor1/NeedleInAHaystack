package org.project.state;

public class AppState {
    public State state;

    public AppState(){
        this.state = State.Running;
    }

    public void stop() {
        this.state = AppState.State.Stopping;
    }

    public boolean shouldRun() {
        return this.state.equals(State.Running);
    }

    public enum State{
        Running,
        Stopping
    }
}

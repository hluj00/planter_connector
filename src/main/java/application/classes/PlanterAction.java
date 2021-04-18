package application.classes;

import java.sql.Timestamp;

public class PlanterAction {
    private int id;
    private int type;
    private Timestamp createdAt;
    private Timestamp executedAt;
    private int planterId;
    private boolean executed;

    public PlanterAction(int id, int type, Timestamp createdAt, Timestamp executedAt, int planterId, boolean executed) {
        this.id = id;
        this.type = type;
        this.createdAt = createdAt;
        this.executedAt = executedAt;
        this.planterId = planterId;
        this.executed = executed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(Timestamp executedAt) {
        this.executedAt = executedAt;
    }

    public int getPlanterId() {
        return planterId;
    }

    public void setPlanterId(int planterId) {
        this.planterId = planterId;
    }

    public boolean isExecuted() {
        return executed;
    }

    public void setExecuted(boolean executed) {
        this.executed = executed;
    }
}

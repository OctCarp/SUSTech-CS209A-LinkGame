package io.github.octcarp.linkgame.common.module;

public record PlayerRecord(String id, String password) {
    public PlayerRecord {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id cannot be null or empty");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("password cannot be null or empty");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PlayerRecord playerRecord = (PlayerRecord) obj;
        return id.equals(playerRecord.id) && password.equals(playerRecord.password);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

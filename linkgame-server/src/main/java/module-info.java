module linkgame.server {
    requires transitive linkgame.common;
    requires org.apache.commons.csv;
    exports io.github.octcarp.linkgame.server;
    exports io.github.octcarp.linkgame.server.net;
}
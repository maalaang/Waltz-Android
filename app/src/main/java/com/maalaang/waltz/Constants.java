package com.maalaang.waltz;

public class Constants {

	public final static String ROOM_URL = "http://dev.maalaang.com/redhair/Sunrise/d/room/info/?name=";
	public final static String CHANNEL_URL = "ws://maalaang.vps.phps.kr:8888/sunrise/channel/";
	public final static String ICE_CONFIG = "{ \"iceServers\" : [ {"
			+ "\"stun\" : \"stun:27.102.207.172\"},"
			+ "{\"turn\" : \"turn:27.102.207.172:3478\","
			+ "\"credential\" : \"whale\","
			+ "\"password\" : \"whale88\"}"
			+ "]}";

    public final static String PACKEGE = "com.maalaang.waltz";
    public final static String DBNAME = "Waltz.db";
}

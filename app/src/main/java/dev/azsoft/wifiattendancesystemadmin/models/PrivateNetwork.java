package dev.azsoft.wifiattendancesystemadmin.models;

public class PrivateNetwork {
    String id;
    String ssid;
    String bssid;
    int networkId;
    int ipAddress;
    String macAddress;
    long timestamp;
    String addedByUID;

    public PrivateNetwork() {
    }

    public PrivateNetwork(String id, String ssid, String bssid, int networkId, int ipAddress, String macAddress, long timestamp, String addedByUID) {
        this.id = id;
        this.ssid = ssid;
        this.bssid = bssid;
        this.networkId = networkId;
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.timestamp = timestamp;
        this.addedByUID = addedByUID;
    }

    public String getId() {
        return id;
    }

    public PrivateNetwork setId(String id) {
        this.id = id;
        return this;
    }

    public String getSsid() {
        return ssid;
    }

    public PrivateNetwork setSsid(String ssid) {
        this.ssid = ssid;
        return this;
    }

    public String getBssid() {
        return bssid;
    }

    public PrivateNetwork setBssid(String bssid) {
        this.bssid = bssid;
        return this;
    }

    public int getNetworkId() {
        return networkId;
    }

    public PrivateNetwork setNetworkId(int networkId) {
        this.networkId = networkId;
        return this;
    }

    public int getIpAddress() {
        return ipAddress;
    }

    public PrivateNetwork setIpAddress(int ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public PrivateNetwork setMacAddress(String macAddress) {
        this.macAddress = macAddress;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public PrivateNetwork setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getAddedByUID() {
        return addedByUID;
    }

    public PrivateNetwork setAddedByUID(String addedByUID) {
        this.addedByUID = addedByUID;
        return this;
    }
}



/*
 *     This file is part of snapcast
 *     Copyright (C) 2014-2018  Johannes Pohl
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.badaix.snapcast;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;

import de.badaix.snapcast.control.json.Client;
import de.badaix.snapcast.control.json.Volume;

public class ClientItem extends LinearLayout implements SeekBar.OnSeekBarChangeListener, View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private static final String TAG = "ClientItem";

    private final TextView title;
    private final SeekBar volumeSeekBar;
    private final ImageButton ibMute;
    private final ImageButton ibOverflow;
    private final ImageButton ibOpenWeb;
    private Client client;
    private ClientItemListener listener = null;

    public ClientItem(Context context) {
        this(context, null);
    }

    public ClientItem(Context context, Client client) {
        super(context);
        LayoutInflater vi = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vi.inflate(R.layout.client_item, this);
        title = findViewById(R.id.title);
        volumeSeekBar = findViewById(R.id.volumeSeekBar);
        ibMute = findViewById(R.id.ibMute);
        ibMute.setImageResource(R.drawable.volume_up_24px);
        ibMute.setOnClickListener(this);
        ibOpenWeb = findViewById(R.id.ibOpenWeb);
        ibOpenWeb.setOnClickListener(this);
        ibOverflow = findViewById(R.id.ibOverflow);
        ibOverflow.setOnClickListener(this);
        volumeSeekBar.setOnSeekBarChangeListener(this);
        setClient(client);
    }

    public void update() {
        //Log.d(TAG, "update: " + client.getVisibleName() + ", connected: " + client.isConnected());
        if (!client.getVisibleName().equals(title.getText()))
            title.setText(client.getVisibleName());
        title.setEnabled(client.isConnected());
        volumeSeekBar.setProgress(client.getConfig().getVolume().getPercent());
        applyMuteUiState();
    }

    public Client getClient() {
        return client;
    }

    public void setClient(final Client client) {
        this.client = client;
        update();
    }

    private void applyMuteUiState() {
        if (client == null)
            return;

        Volume volume = client.getConfig().getVolume();
        boolean muted = volume.isMuted();

        int muteRed = ContextCompat.getColor(getContext(), R.color.mute_red);
        int muteBlue = ContextCompat.getColor(getContext(), R.color.colorPrimary);

        if (muted) {
            ibMute.setImageResource(R.drawable.volume_off_24px);
            ibMute.setColorFilter(muteRed);
            volumeSeekBar.setProgressTintList(ColorStateList.valueOf(muteRed));
            volumeSeekBar.setThumbTintList(ColorStateList.valueOf(muteRed));
        } else {
            ibMute.setImageResource(R.drawable.volume_up_24px);
            ibMute.setColorFilter(muteBlue);
            volumeSeekBar.setProgressTintList(ColorStateList.valueOf(muteBlue));
            volumeSeekBar.setThumbTintList(ColorStateList.valueOf(muteBlue));
        }
    }

    public void setListener(ClientItemListener listener) {
        this.listener = listener;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser && (listener != null)) {
            Volume volume = client.getConfig().getVolume();
            volume.setPercent(progress);
            listener.onVolumeChanged(this, volume.getPercent(), volume.isMuted());
        }
    }

    @Override
    public void onClick(View v) {
        if (v == ibMute) {
            Volume volume = client.getConfig().getVolume();
            volume.setMuted(!volume.isMuted());
            update();
            listener.onVolumeChanged(this, volume.getPercent(), volume.isMuted());
        } else if (v == ibOpenWeb) {
            openClientWebPage();
        } else if (v == ibOverflow) {
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            popup.getMenu().add(Menu.NONE, R.id.menu_details, 0, R.string.menu_details);
            if (!client.isConnected())
                popup.getMenu().add(Menu.NONE, R.id.menu_delete, 1, R.string.menu_delete);
            popup.setOnMenuItemClickListener(this);
            popup.show();
        }
    }

    private void openClientWebPage() {
        if (client == null || client.getHost() == null)
            return;

        String ipRaw = client.getHost().getIp();
        if ((ipRaw == null) || ipRaw.isEmpty())
            return;

        String ip = ipRaw.trim();

        // Extract plain IPv4 part from values like "::ffff:192.168.0.115"
        Pattern ipv4Pattern = Pattern.compile("(\\d+\\.\\d+\\.\\d+\\.\\d+)");
        Matcher matcher = ipv4Pattern.matcher(ip);
        if (matcher.find()) {
            ip = matcher.group(1);
        }

        String url;
        // If the IP already includes a scheme, use it as-is
        if (ip.startsWith("http://") || ip.startsWith("https://")) {
            url = ip;
        } else {
            // Just use the plain IPv4 (and optional port if already present)
            url = "http://" + ip;
        }

        Toast.makeText(getContext(), url, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getContext(), ClientWebActivity.class);
        intent.putExtra(ClientWebActivity.EXTRA_URL, url);
        getContext().startActivity(intent);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_details:
                listener.onPropertiesClicked(this);
                return true;
            case R.id.menu_delete:
                listener.onDeleteClicked(this);
                return true;
            default:
                return false;
        }
    }

    public interface ClientItemListener {
        void onVolumeChanged(ClientItem clientItem, int percent, boolean mute);

        void onDeleteClicked(ClientItem clientItem);

        void onPropertiesClicked(ClientItem clientItem);
    }

}

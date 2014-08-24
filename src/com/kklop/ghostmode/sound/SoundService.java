/*******************************************************************************
 * Copyright 2012-2014 Kevin Klopfenstein.
 *
 * This file is part of GhostMode.
 *
 * GhostMode is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GhostMode is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GhostMode.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package com.kklop.ghostmode.sound;

import java.io.FileDescriptor;
import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;


public class SoundService implements OnCompletionListener {
	MediaPlayer mediaPlayer;
    boolean isPrepared = false;
     
    protected SoundService(AssetFileDescriptor assetDescriptor){
        mediaPlayer = new MediaPlayer();
        try{
            mediaPlayer.setDataSource(assetDescriptor.getFileDescriptor(), 
            		assetDescriptor.getStartOffset(), 
            		assetDescriptor.getLength());
            mediaPlayer.prepare();
            isPrepared = true;
            mediaPlayer.setOnCompletionListener(this);
        } catch(Exception ex){
            throw new RuntimeException("Couldn't load music, uh oh!");
        }
    }
     
    protected SoundService(FileDescriptor fileDescriptor){
        mediaPlayer = new MediaPlayer();
        try{
            mediaPlayer.setDataSource(fileDescriptor);
            mediaPlayer.prepare();
            isPrepared = true;
            mediaPlayer.setOnCompletionListener(this);
        } catch(Exception ex){
            throw new RuntimeException("Couldn't load music, uh oh!");
        }
    }
     
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        synchronized(this){
            isPrepared = false;
        }
    }
 
    public void play() {
        if(mediaPlayer.isPlaying()){
            return;
        }
        try{
            synchronized(this){
                if(!isPrepared){
                    mediaPlayer.prepare();
                }
                mediaPlayer.start();
            }
        } catch(IllegalStateException ex){
            ex.printStackTrace();
        } catch(IOException ex){
            ex.printStackTrace();
        }
    }
 
    public void stop() {
        mediaPlayer.stop();
        synchronized(this){
            isPrepared = false;
        }
    }
     
    public void switchTracks(){
        mediaPlayer.seekTo(0);
        mediaPlayer.pause();
    }
     
    public void pause() {
        mediaPlayer.pause();
    }
 
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }
     
    public boolean isLooping() {
        return mediaPlayer.isLooping();
    }
     
    public void setLooping(boolean isLooping) {
        mediaPlayer.setLooping(isLooping);
    }
 
    public void setVolume(float volumeLeft, float volumeRight) {
        mediaPlayer.setVolume(volumeLeft, volumeRight);
    }
 
    public void dispose() {
        if(mediaPlayer.isPlaying()){
            stop();
        }
        mediaPlayer.release();
    }

}

package com.miv_sher.plainolnotes.viewmodel;

import android.app.Application;

import com.miv_sher.plainolnotes.database.AppRepository;
import com.miv_sher.plainolnotes.database.NoteEntity;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class EditorViewModel extends AndroidViewModel {
    public MutableLiveData<NoteEntity> mLiveNote = new MutableLiveData<>();
    private AppRepository mRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public EditorViewModel(@NonNull Application application) {
        super(application);

        mRepository = AppRepository.getInstance(application.getApplicationContext());
    }

    public void loadData(final int noteId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                NoteEntity noteEntity = mRepository.getNoteById(noteId);
                mLiveNote.postValue(noteEntity);
            }
        });
    }

    public void saveNote(String noteText) {
        NoteEntity noteEntity = mLiveNote.getValue();
        if (noteEntity == null) {
            noteEntity = new NoteEntity(new Date(), noteText);
        } else {
            noteEntity.setText(noteText);
        }

        mRepository.insertNote(noteEntity);
    }
}

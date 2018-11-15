package com.r4hu7.justnoteit.di.module;

import android.content.Context;

import com.r4hu7.justnoteit.data.NotesRepository;
import com.r4hu7.justnoteit.data.local.LocalDataSource;
import com.r4hu7.justnoteit.data.local.NotesDatabase;
import com.r4hu7.justnoteit.utils.AppExecutors;

import dagger.Module;
import dagger.Provides;

@Module(includes = ContextModule.class)
public class RepositoryModule {
    @Provides
    public NotesRepository notesRepository(LocalDataSource localDataSource) {
        return NotesRepository.getInstance(localDataSource);
    }

    @Provides
    public LocalDataSource localDataSource(AppExecutors appExecutors, NotesDatabase database) {
        return LocalDataSource.getInstance(appExecutors, database);
    }

    @Provides
    public NotesDatabase notesDatabase(Context context) {
        return NotesDatabase.getInstance(context);
    }

    @Provides
    public AppExecutors appExecutors() {
        return new AppExecutors();
    }
}

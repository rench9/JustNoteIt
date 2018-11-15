package com.r4hu7.justnoteit.di.component;

import com.r4hu7.justnoteit.data.NotesRepository;
import com.r4hu7.justnoteit.di.module.RepositoryModule;

import dagger.Component;

@Component(modules = {RepositoryModule.class})
public interface RepositoryComponent {
    NotesRepository getNotesRepository();
}

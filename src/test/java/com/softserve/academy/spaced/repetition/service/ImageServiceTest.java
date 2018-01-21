package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Image;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.repository.ImageRepository;
import com.softserve.academy.spaced.repetition.service.impl.ImageServiceImpl;
import com.softserve.academy.spaced.repetition.util.DomainFactory;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class ImageServiceTest {

    @InjectMocks
    private ImageServiceImpl imageService;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private UserService userService;

    private User user;
    private Image image;

    private final long USER_ID = 1L;
    private final long IMAGE_ID = 1L;

    @Before
    public void setUp() {
        user = DomainFactory.createUser(USER_ID, null, null, null, null);
        image = DomainFactory.createImage(IMAGE_ID, null, null, user, null, false);
    }

    @Test
    public void testAddImageToDB() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenReturn(user);
        when(imageRepository.save(image)).thenReturn(image);
    }

}

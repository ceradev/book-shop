package com.backend.library.backend.services.interfaces;

import java.util.List;
import com.backend.library.backend.dto.authors.AuthorDetailsDTO;
import com.backend.library.backend.dto.authors.RequestAuthorDTO;

public interface IAuthorService {

    List<AuthorDetailsDTO> getAll();

    AuthorDetailsDTO getAuthor(Long id);

    RequestAuthorDTO add(RequestAuthorDTO author);

    RequestAuthorDTO update(RequestAuthorDTO author, Long id);

    void delete(Long id);

}

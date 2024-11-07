package com.OEzoa.OEasy.domain.recipe;

import org.apache.el.stream.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OeRecipeManualRepository extends JpaRepository<OeRecipeManual, Long> {


}

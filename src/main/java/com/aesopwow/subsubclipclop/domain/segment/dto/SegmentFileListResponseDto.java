package com.aesopwow.subsubclipclop.domain.segment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SegmentFileListResponseDto {
    private List<String> files;
}

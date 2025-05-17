package com.aesopwow.subsubclipclop.domain.analysis.repository;

import com.aesopwow.subsubclipclop.domain.analysis.dto.cohortdouble.CohortDoubleAnalysisInsightResponseDto;
import com.aesopwow.subsubclipclop.domain.analysis.dto.cohortdouble.CohortDoubleAnalysisRemainHeatmapRequestDto;
import com.aesopwow.subsubclipclop.domain.analysis.dto.cohortdouble.CohortDoubleAnalysisRemainHeatmapResponseDto;
import com.aesopwow.subsubclipclop.domain.analysis.dto.cohortdouble.CohortDoubleAnalysisUserDataRequestDto;
import com.aesopwow.subsubclipclop.domain.analysis.dto.cohortdouble.CohortDoubleAnalysisUserDataResponseDto;
import com.aesopwow.subsubclipclop.domain.analysis.dto.cohortdouble.CohortDoubleAnalysisVisualizationRequestDto;
import com.aesopwow.subsubclipclop.domain.analysis.dto.cohortdouble.CohortDoubleAnalysisVisualizationResponseDto;
import com.aesopwow.subsubclipclop.domain.analysis.dto.cohortsingle.CohortSingleAnalysisInsightResponseDto;
import com.aesopwow.subsubclipclop.domain.analysis.dto.cohortsingle.CohortSingleAnalysisRemainHeatmapRequestDto;
import com.aesopwow.subsubclipclop.domain.analysis.dto.cohortsingle.CohortSingleAnalysisRemainHeatmapResponseDto;
import com.aesopwow.subsubclipclop.domain.analysis.dto.cohortsingle.CohortSingleAnalysisUserDataRequestDto;
import com.aesopwow.subsubclipclop.domain.analysis.dto.cohortsingle.CohortSingleAnalysisUserDataResponseDto;
import com.aesopwow.subsubclipclop.domain.analysis.dto.cohortsingle.CohortSingleAnalysisVisualizationResponseDto;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Repository
public class AnalysisRepositoryImpl implements AnalysisRepository {

    // ========== SINGLE ==========

    @Override
    public CohortSingleAnalysisInsightResponseDto fetchSingleInsight(String clusterType) {
        try (BufferedReader reader = getReader("mock/single-insight.csv")) {
            reader.readLine();
            String[] parts = reader.readLine().split(",");
            return new CohortSingleAnalysisInsightResponseDto(parts[0].trim());
        } catch (Exception e) {
            throw new RuntimeException("단일 인사이트 분석 실패", e);
        }
    }

    @Override
    public CohortSingleAnalysisRemainHeatmapResponseDto fetchSingleRemainHeatmap(CohortSingleAnalysisRemainHeatmapRequestDto requestDto) {
        try (BufferedReader reader = getReader("mock/single-remain-heatmap.csv")) {
            String line;
            String content = "";
            List<String> columnLabels = new ArrayList<>();
            List<List<String>> dataRows = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                switch (line.trim()) {
                    case "content" -> content = reader.readLine().trim();
                    case "columns" -> columnLabels = Arrays.asList(reader.readLine().split(","));
                    case "rows" -> {
                        while ((line = reader.readLine()) != null) {
                            String[] parts = line.split(",");
                            List<String> row = new ArrayList<>(Arrays.asList(parts));
                            dataRows.add(row);
                        }
                    }
                }
            }

            return new CohortSingleAnalysisRemainHeatmapResponseDto(content, columnLabels, dataRows);
        } catch (Exception e) {
            throw new RuntimeException("단일 히트맵 분석 실패", e);
        }
    }

    @Override
    public CohortSingleAnalysisVisualizationResponseDto fetchSingleVisualization(String clusterType) {
        try (BufferedReader reader = getReader("mock/single-visualization.csv")) {
            reader.readLine();
            String[] parts = reader.readLine().split(",");
            return new CohortSingleAnalysisVisualizationResponseDto(
                    encodeBase64(parts[0].trim()),
                    encodeBase64(parts[1].trim()));
        } catch (Exception e) {
            throw new RuntimeException("단일 시각화 분석 실패", e);
        }
    }

    @Override
    public CohortSingleAnalysisUserDataResponseDto fetchSingleUserData(CohortSingleAnalysisUserDataRequestDto requestDto) {
        try (BufferedReader reader = getReader("mock/single-user-data.csv")) {
            String[] headers = reader.readLine().split(",");
            List<Map<String, String>> table = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                Map<String, String> row = new LinkedHashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    if (requestDto.getFields().contains(headers[i].trim())) {
                        row.put(headers[i].trim(), values[i].trim());
                    }
                }
                table.add(row);
            }

            return new CohortSingleAnalysisUserDataResponseDto(table);
        } catch (Exception e) {
            throw new RuntimeException("단일 유저 데이터 로딩 실패", e);
        }
    }

    // ========== DOUBLE ==========

    @Override
    public CohortDoubleAnalysisInsightResponseDto fetchDoubleInsight(String firstClusterType, String secondClusterType) {
        try (BufferedReader reader = getReader("mock/double-insight.csv")) {
            reader.readLine();
            String[] parts = reader.readLine().split(",");
            return new CohortDoubleAnalysisInsightResponseDto(parts[0].trim(), parts[1].trim());
        } catch (Exception e) {
            throw new RuntimeException("이중 인사이트 분석 실패", e);
        }
    }

    @Override
    public CohortDoubleAnalysisRemainHeatmapResponseDto fetchDoubleRemainHeatmap(CohortDoubleAnalysisRemainHeatmapRequestDto requestDto) {
        try (BufferedReader reader = getReader("mock/double-remain-heatmap.csv")) {
            String line;
            String firstContent = "";
            List<String> firstColumnLabels = new ArrayList<>();
            List<List<String>> firstDataRows = new ArrayList<>();
            String secondContent = "";
            List<String> secondColumnLabels = new ArrayList<>();
            List<List<String>> secondDataRows = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                switch (line.trim()) {
                    case "firstContent" -> firstContent = reader.readLine().trim();
                    case "firstColumns" -> firstColumnLabels = Arrays.asList(reader.readLine().split(","));
                    case "firstRows" -> {
                        while ((line = reader.readLine()) != null && !line.equals("secondContent")) {
                            String[] parts = line.split(",");
                            List<String> row = new ArrayList<>(Arrays.asList(parts));
                            firstDataRows.add(row);
                        }
                    }
                    case "secondContent" -> secondContent = reader.readLine().trim();
                    case "secondColumns" -> secondColumnLabels = Arrays.asList(reader.readLine().split(","));
                    case "secondRows" -> {
                        while ((line = reader.readLine()) != null) {
                            String[] parts = line.split(",");
                            List<String> row = new ArrayList<>(Arrays.asList(parts));
                            secondDataRows.add(row);
                        }
                    }
                }
            }

            return new CohortDoubleAnalysisRemainHeatmapResponseDto(
                    firstContent, firstColumnLabels, firstDataRows,
                    secondContent, secondColumnLabels, secondDataRows
            );
        } catch (Exception e) {
            throw new RuntimeException("이중 히트맵 분석 실패", e);
        }
    }

    @Override
    public CohortDoubleAnalysisVisualizationResponseDto fetchDoubleVisualization(CohortDoubleAnalysisVisualizationRequestDto requestDto) {
        try (BufferedReader reader = getReader("mock/double-visualization.csv")) {
            reader.readLine();
            String[] parts = reader.readLine().split(",");

            return new CohortDoubleAnalysisVisualizationResponseDto(
                    encodeBase64(parts[0].trim()),
                    encodeBase64(parts[1].trim()),
                    encodeBase64(parts[2].trim()),
                    encodeBase64(parts[3].trim())
            );
        } catch (Exception e) {
            throw new RuntimeException("이중 시각화 분석 실패", e);
        }
    }

    @Override
    public CohortDoubleAnalysisUserDataResponseDto fetchDoubleUserData(CohortDoubleAnalysisUserDataRequestDto requestDto) {
        try (BufferedReader reader = getReader("mock/double-user-data.csv")) {
            String[] headers = reader.readLine().split(",");
            List<Map<String, String>> firstTable = new ArrayList<>();
            List<Map<String, String>> secondTable = new ArrayList<>();
            String line;
            boolean readingFirst = true;

            List<String> fields = Optional.ofNullable(requestDto.getFields()).orElse(Collections.emptyList());

            while ((line = reader.readLine()) != null) {
                if (line.trim().equals("---")) {
                    readingFirst = false;
                    continue;
                }

                String[] values = line.split(",", -1); // 빈 값 포함 split
                if (values.length != headers.length) {
                    System.out.println("⚠️ 값 개수가 헤더와 다릅니다: " + Arrays.toString(values));
                    continue;
                }

                Map<String, String> row = new LinkedHashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    String header = headers[i].trim();
                    String value = values[i].trim();

                    // field 비교 시 공백 제거 및 대소문자 무시
                    boolean matches = fields.stream()
                            .anyMatch(f -> f.trim().equalsIgnoreCase(header));

                    if (matches) {
                        row.put(header, value);
                    }
                }

                if (!row.isEmpty()) {
                    if (readingFirst) firstTable.add(row);
                    else secondTable.add(row);
                }
            }

            return new CohortDoubleAnalysisUserDataResponseDto(firstTable, secondTable);
        } catch (Exception e) {
            throw new RuntimeException("이중 유저 데이터 로딩 실패", e);
        }
    }

    private BufferedReader getReader(String resourcePath) throws IOException {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (stream == null) throw new IllegalArgumentException("CSV 파일 없음: " + resourcePath);
        return new BufferedReader(new InputStreamReader(stream));
    }

    private String encodeBase64(String imagePath) throws IOException {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(imagePath);
        if (stream == null) throw new IllegalArgumentException("이미지 파일 없음: " + imagePath);
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(stream.readAllBytes());
    }
}
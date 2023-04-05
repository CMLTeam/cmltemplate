package com.cmlteam.cmltemplate.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.Test;

class JsonUtilTest {
  private static final String INVALID_JSON = "},z";

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  private static class TestClass {
    private int i;
    private String s;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  private static class A {
    private TestClass t;
  }


  @Test
  void convertToJsonStringShouldSucceed() {
    assertEquals("{\"i\":5,\"s\":\"str\"}", JsonUtil.toJsonString(new TestClass(5, "str")));
  }

  @Test
  void parseListShouldSucceed() {
    // GIVEN

    String json = "[{\"i\":5,\"s\":\"str\"}, {\"i\":6,\"s\":\"other str\"}]";

    // WHEN

    List<?> objects = JsonUtil.parseList(json);

    // THEN

    assertEquals(2, objects.size());
    assertEquals(5, ((Map) objects.get(0)).get("i"));
    assertEquals(6, ((Map) objects.get(1)).get("i"));
    assertEquals("str", ((Map) objects.get(0)).get("s"));
    assertEquals("other str", ((Map) objects.get(1)).get("s"));
  }

  @Test
  void parseListInvalidJsonShouldFail() {
    assertThrows(JsonUtil.JsonParseException.class, () -> JsonUtil.parseList(INVALID_JSON));
  }

  @Test
  void parseJsonShouldSucceed() {
    // GIVEN

    String json = "{\"i\":5,\"s\":\"str\"}";

    // WHEN

    Map map = JsonUtil.parseJson(json);

    // THEN

    assertEquals(5, map.get("i"));
    assertEquals("str", map.get("s"));
  }

  @Test
  void parseJsonInvalidJsonShouldFail() {
    assertThrows(JsonUtil.JsonParseException.class, () -> JsonUtil.parseJson(INVALID_JSON));
  }

  @Test
  void parseJsonClassShouldSucceed() {
    // GIVEN

    String json = "{\"i\":5,\"s\":\"str\"}";

    // WHEN

    TestClass object = JsonUtil.parseJson(json, TestClass.class);

    // THEN

    assertEquals(5, object.getI());
    assertEquals("str", object.getS());
  }

  @Test
  void parseJsonClassInvalidJsonShouldFail() {
    assertThrows(
        JsonUtil.JsonParseException.class, () -> JsonUtil.parseJson(INVALID_JSON, TestClass.class));
  }

  @Test
  void parseJsonListShouldSucceed() {
    // GIVEN

    String json = "[{\"i\":5,\"s\":\"str\"}, {\"i\":6,\"s\":\"other str\"}]";

    // WHEN

    List<TestClass> objects = JsonUtil.parseJsonList(json, TestClass.class);

    // THEN

    assertEquals(2, objects.size());
    assertEquals(5, objects.get(0).getI());
    assertEquals(6, objects.get(1).getI());
    assertEquals("str", objects.get(0).getS());
    assertEquals("other str", objects.get(1).getS());
  }

  @Test
  void parseJsonListInvalidJsonShouldFail() {
    assertThrows(
        JsonUtil.JsonParseException.class,
        () -> JsonUtil.parseJsonList(INVALID_JSON, TestClass.class));
  }

  @Test
  void prettyPrintJsonShouldSucceed() {
    // GIVEN

    String json = "{\"i\":5,\"s\":\"str\"}";

    // WHEN

    String prettyPrintedJson = JsonUtil.prettyPrintJson(json);

    // THEN

    assertEquals(
        "{"
            + System.lineSeparator()
            + "  \"i\" : 5,"
            + System.lineSeparator()
            + "  \"s\" : \"str\""
            + System.lineSeparator()
            + "}",
        prettyPrintedJson);
  }

  @Test
  void prettyPrintJsonNotJsonShouldNotChange() {
    // GIVEN

    String notAJson = "not a json: { should not change ()%$ }";

    // WHEN

    String prettyPrintedJson = JsonUtil.prettyPrintJson(notAJson);

    // THEN

    assertEquals(notAJson, prettyPrintedJson);
  }

  @Test
  void jsonBuilderProduceJson() {
    // WHEN
    String json = JsonUtil.json().add("i", 5).add("s", "str").toString();

    // THEN
    TestClass object = JsonUtil.parseJson(json, TestClass.class);
    assertEquals(5, object.getI());
    assertEquals("str", object.getS());
  }

  @Test
  void jsonBuilderProduceJson1() {
    // WHEN
    String json = JsonUtil.json().add("t", JsonUtil.json().add("i", 5).add("s", "str")).toString();

    // THEN
    A object = JsonUtil.parseJson(json, A.class);
    assertEquals(5, object.t.getI());
    assertEquals("str", object.t.getS());
  }

  @Test
  void jsonBuilderListShouldSucceed() {
    // WHEN
    String json =
        JsonUtil.json()
            .add("list", List.of(Map.of("i", 5, "s", "str"), Map.of("i", 6, "s", "other str")))
            .toString();

    // THEN
    Map map = JsonUtil.parseJson(json);

    List<?> objects = (List<?>) map.get("list");

    assertEquals(2, objects.size());
    assertEquals(5, ((Map) objects.get(0)).get("i"));
    assertEquals(6, ((Map) objects.get(1)).get("i"));
    assertEquals("str", ((Map) objects.get(0)).get("s"));
    assertEquals("other str", ((Map) objects.get(1)).get("s"));
  }
}

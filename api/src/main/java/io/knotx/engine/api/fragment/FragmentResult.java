/*
 * Copyright (C) 2019 Knot.x Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.knotx.engine.api.fragment;

import io.knotx.fragment.Fragment;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

@DataObject
public class FragmentResult {

  public static final String DEFAULT_TRANSITION = "_next";
  public static final String ERROR_TRANSITION = "_error";

  private static final String FRAGMENT_KEY = "fragment";
  private static final String TRANSITION_KEY = "transition";

  private Fragment fragment;
  private String transition;

  public FragmentResult(Fragment fragment, String transition) {
    this.fragment = fragment;
    this.transition = transition;
  }

  public FragmentResult(JsonObject json) {
    this.fragment = new Fragment(json.getJsonObject(FRAGMENT_KEY));
    this.transition = json.getString(TRANSITION_KEY);
  }

  public JsonObject toJson() {
    return new JsonObject()
        .put(FRAGMENT_KEY, fragment.toJson())
        .put(TRANSITION_KEY, transition);
  }

  public Fragment getFragment() {
    return fragment;
  }

  public String getTransition() {
    if (StringUtils.isBlank(transition)) {
      return DEFAULT_TRANSITION;
    } else {
      return transition;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FragmentResult that = (FragmentResult) o;
    return Objects.equals(fragment, that.fragment) &&
        Objects.equals(transition, that.transition);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fragment, transition);
  }

  @Override
  public String toString() {
    return "FragmentResult{" +
        "fragment=" + fragment +
        ", transition='" + transition + '\'' +
        '}';
  }
}
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
 *
 * The code comes from https://github.com/tomaszmichalak/vertx-rx-map-reduce.
 */
package io.knotx.engine.handler;

import io.knotx.engine.api.KnotFlow;
import io.knotx.fragment.Fragment;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

public class KnotFlowProvider {

  private final Logger LOGGER = LoggerFactory.getLogger(KnotFlowProvider.class);

  private List<KnotFlowContext> flows;

  KnotFlowProvider(List<KnotFlowContext> flows) {
    this.flows = flows;
  }

  public Optional<KnotFlow> get(Fragment fragment) {
    Optional<KnotFlow> result = Optional.empty();
    if (fragment.getConfiguration().containsKey("flowName")) {
      result = flows.stream()
          .filter(flowContext -> flowContext.getName()
              .equals(fragment.getConfiguration().getString("flowName")))
          .map(KnotFlowContext::getKnotFlow)
          .findFirst();
    } else if (fragment.getConfiguration().containsKey("flow")) {
      try {
        result = parseString(fragment.getConfiguration().getString("flow"));
      } catch (DecodeException ex) {
        LOGGER.error("Could not extract [flow] attribute from fragment [{}]", fragment);
        result = Optional.empty();
      }
    }
    return result;
  }

  private Optional<KnotFlow> parseString(String encoded) {
    if (StringUtils.isBlank(encoded)) {
      return Optional.empty();
    }
    if (encoded.startsWith("{")) {
      KnotFlow knotFlow = new KnotFlow(new JsonObject(encoded));
      if (StringUtils.isNotBlank(knotFlow.getAddress())) {
        return Optional.of(knotFlow);
      } else {
        return Optional.empty();
      }
    } else {
      String[] split = encoded.split(",");
      KnotFlow knotFlow = null;
      for (int i = split.length - 1; i >= 0; i--) {
        if (knotFlow == null) {
          knotFlow = new KnotFlow(split[i], Collections.emptyMap());
        } else {
          knotFlow = new KnotFlow(split[i], Collections.singletonMap("next", knotFlow));
        }
      }
      return Optional.of(knotFlow);
    }
  }

}
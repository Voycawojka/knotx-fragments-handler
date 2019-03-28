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
package io.knotx.fragments.graph;

import io.knotx.fragment.Fragment;
import io.knotx.fragments.engine.GraphNode;
import io.knotx.fragments.handler.action.ActionProvider;
import io.knotx.fragments.handler.api.fragment.Action;
import io.knotx.fragments.handler.api.fragment.FragmentContext;
import io.knotx.fragments.handler.api.fragment.FragmentResult;
import io.knotx.fragments.handler.exception.GraphConfigurationException;
import io.knotx.fragments.handler.options.GraphOptions;
import io.reactivex.Single;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class GraphBuilder {

  private static final Logger LOGGER = LoggerFactory.getLogger(GraphBuilder.class);

  private static final String TASK_KEY = "data-knotx-task";


  private final Map<String, GraphOptions> graphOptionsMap;
  private final ActionProvider proxyProvider;

  public GraphBuilder(Map<String, GraphOptions> graphOptionsMap,
      ActionProvider proxyProvider) {
    this.graphOptionsMap = graphOptionsMap;
    this.proxyProvider = proxyProvider;
  }

  public Optional<GraphNode> build(Fragment fragment) {
    Optional<GraphNode> result = Optional.empty();
    if (fragment.getConfiguration().containsKey(TASK_KEY)) {
      String graphAlias = fragment.getConfiguration().getString(TASK_KEY);
      GraphOptions options = graphOptionsMap.get(graphAlias);
      result = Optional.of(initGraphNode(graphAlias, options));
    }
    return result;
  }

  private GraphNode initGraphNode(String alias, GraphOptions options) {
    Action action = proxyProvider.get(options.getProxy()).orElseThrow(
        () -> new GraphConfigurationException("No provider for proxy " + options.getProxy()));

    Map<String, GraphOptions> transitions = options.getTransitions();
    Map<String, GraphNode> edges = new HashMap<>();
    transitions.forEach((transition, childGraphOptions) -> {
      GraphNode node = initGraphNode(alias, childGraphOptions);
      edges.put(transition, node);
    });
    return new GraphNode(alias, toRxFunction(action), edges);

  }

  private Function<FragmentContext, Single<FragmentResult>> toRxFunction(
      Action action) {
    io.knotx.fragments.handler.reactivex.api.fragment.Action rxAction = io.knotx.fragments.handler.reactivex.api.fragment.Action
        .newInstance(action);
    return rxAction::rxApply;
  }

}
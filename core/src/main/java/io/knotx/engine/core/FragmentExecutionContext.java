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
package io.knotx.engine.core;

import io.knotx.engine.api.FragmentEventContext;
import io.knotx.engine.api.GraphNode;

public class FragmentExecutionContext {

  private FragmentEventContext fragmentEventContext;

  private GraphNode currentNode;

  public FragmentEventContext getFragmentEventContext() {
    return fragmentEventContext;
  }

  public FragmentExecutionContext setFragmentEventContext(
      FragmentEventContext fragmentEventContext) {
    this.fragmentEventContext = fragmentEventContext;
    return this;
  }

  public GraphNode getCurrentNode() {
    return currentNode;
  }

  public FragmentExecutionContext setCurrentNode(GraphNode graphNode) {
    this.currentNode = graphNode;
    return this;
  }

  public FragmentExecutionContext end() {
    currentNode = null;
    return this;
  }

  public boolean isLast() {
    return currentNode == null;
  }

}
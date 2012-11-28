package com.dianping.phoenix.console.page.deploy;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.dianping.phoenix.deploy.model.entity.DeployModel;
import com.dianping.phoenix.deploy.model.entity.HostModel;
import com.dianping.phoenix.deploy.model.entity.SegmentModel;
import com.dianping.phoenix.deploy.model.transform.BaseVisitor;

class StatusModelVisitor extends BaseVisitor {
	private DeployModel m_model;

	private Map<String, Integer> m_map;

	public StatusModelVisitor(Map<String, Integer> map) {
		m_map = map != null ? map : Collections.<String, Integer> emptyMap();
	}

	public DeployModel getModel() {
		return m_model;
	}

	@Override
	public void visitDeploy(DeployModel other) {
		m_model = new DeployModel();
		m_model.mergeAttributes(other);

		super.visitDeploy(other);
	}

	@Override
	public void visitHost(HostModel other) {
		String ip = other.getIp();
		List<SegmentModel> segments = other.getSegments();
		int size = segments.size();
		Integer index = m_map.get(ip);
		StringBuilder sb = new StringBuilder(1024);
		int progress = 0;
		String step = null;

		if (index == null) {
			index = 0;
		}

		for (int i = 0; i < index; i++) {
			SegmentModel segment = segments.get(i);

			if (segment.getStep() != null) {
				step = segment.getStep();
			}
		}
		for (int i = index; i < size; i++) {
			SegmentModel segment = segments.get(i);

			if (segment.getTotalTicks() > 0) {
				progress = segment.getCurrentTicks() * 100 / segment.getTotalTicks();
			}

			if (segment.getStep() != null) {
				step = segment.getStep();
			}

			sb.append(segment.getText().trim()).append("\r\n");
		}

		if (sb.length() > 0) {
			HostModel host = new HostModel().setIp(ip);

			host.setProgress(progress);
			host.setCurrentStep(step);
			host.setLog(sb.toString());
			host.setOffset(size);
			m_model.addHost(host);
		}
	}
}
package com.jobrecruitment.repository.common;

import com.jobrecruitment.model.common.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderIdAndReceiverIdOrderBySentAt(Long senderId, Long receiverId);
    List<Message> findByReceiverIdAndIsReadFalse(Long receiverId);
    List<Message> findByApplicationIdOrderBySentAt(Long applicationId);
    Optional<Message> findFirstBySenderIdOrReceiverIdOrderBySentAtDesc(Long senderId, Long receiverId);
    List<Message> findTop5ByReceiverIdAndApplicationIsNotNullOrderBySentAtDesc(Long receiverId);
}

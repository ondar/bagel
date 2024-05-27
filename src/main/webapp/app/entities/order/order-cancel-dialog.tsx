import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity, cancelEntity } from './order.reducer';

export const OrderCancelDialog = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();

  const [loadModal, setLoadModal] = useState(false);

  useEffect(() => {
    dispatch(getEntity(id));
    setLoadModal(true);
  }, []);

  const orderEntity = useAppSelector(state => state.order.entity);
  const updateSuccess = useAppSelector(state => state.order.updateSuccess);

  const handleClose = () => {
    navigate('/order');
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmCancel = () => {
    dispatch(cancelEntity(orderEntity.id));
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="orderCancelDialogHeading">
        Confirm cancel operation
      </ModalHeader>
      <ModalBody id="bagelApp.order.cancel.question">Are you sure you want to cancel Order {orderEntity.id}?</ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Cancel
        </Button>
        <Button id="jhi-confirm-cancel-order" data-cy="entityConfirmCancelButton" color="info" onClick={confirmCancel}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; Cancel Order
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default OrderCancelDialog;
